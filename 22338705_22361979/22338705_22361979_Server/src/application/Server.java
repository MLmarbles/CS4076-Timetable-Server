package application;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class Server {
	public static ServerSocket servSock;
	public static final int PORT = 4554;
	public static ArrayList<Module> modules = new ArrayList<>();
	static int connections = 0;

	public static void main(String[] args) throws SQLException {
	    System.out.println("Opening port...\n");
	    try {
	        servSock = new ServerSocket(PORT);
	        while (true) {
	            Socket client = servSock.accept();
	            connections++;
	            System.out.println("New client connected"
	                        + client.getInetAddress().getHostAddress());
	            System.out.println("Clients connected: " + connections);

	            ClientHandler clientSock = new ClientHandler(client);
	            new Thread(clientSock).start();
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if (servSock != null) {
	            try {
	                servSock.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}


	public static boolean handleClientRequest() {
	    Socket link = null;
	    try {
	        link = servSock.accept();
	        PrintWriter out = new PrintWriter(link.getOutputStream(), true);
	        BufferedReader in = new BufferedReader(new InputStreamReader(link.getInputStream()));

	        String request = in.readLine();
	        String[] parts = request.split(" ", 3);

	        if (parts.length >= 2) {
	            String command = parts[0];
	            String data = parts[1];

	            switch (command) {
	                case "EARLY_LECTURE":
	                    handleEarlyLecture(data, out);
	                    break;

	                case "DISPLAY_SCHEDULE":
	                    handleDisplaySchedule(data, out);
	                    break;

	                case "ADD_MODULE":
	                    handleAddModule(data, out);
	                    break;

	                case "REMOVE_MODULE":
	                    handleRemoveModule(data, out);
	                    break;

	                case "ADD_CLASS":
	                    handleAddClass(data, parts[2], out);
	                    break;

	                case "REMOVE_CLASS":
	                    handleRemoveClass(data, parts[2], out);
	                    break;

	                case "STOP":
	                    out.println("TERMINATE");
	                    link.close();
	                    return false;
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return true;
	}

	private static synchronized void handleEarlyLecture(String data, PrintWriter out) {
	    String earlyModuleFinder = data;
	    Module earlyModule = findModule(earlyModuleFinder);
	    if (earlyModule != null) {
	        ArrayList<Class> classes = earlyModule.getClasses();
	        EarlyLectures_ForkJoin earlyThread = new EarlyLectures_ForkJoin(classes);
	        ForkJoinPool.commonPool().invoke(earlyThread);
	        if (earlyThread.join()) {
	            out.println("Success: Classes shifted to early morning.");
	        } else {
	            out.println("Failure: Unable to shift classes to early morning.");
	        }
	    } else {
	        out.println("Error: Module " + data + " not found.");
	    }
	}


	private static synchronized void handleDisplaySchedule(String data, PrintWriter out) {
	    String moduleCodeToDisplay = data;
	    Module moduleToDisplay = findModule(moduleCodeToDisplay);
	    StringBuilder schedule = new StringBuilder();

	    if (moduleToDisplay != null) {
	        schedule.append("Schedule for Module: ").append(moduleCodeToDisplay);
	        for (Class classObj : moduleToDisplay.getClasses()) {
	            schedule.append(" [");
	            schedule.append(classObj.toString());
	            schedule.append("]");
	        }
	        out.println(schedule.toString());
	    } else {
	        out.println("Error: Module " + moduleCodeToDisplay + " not found.");
	    }
	}

	private static synchronized void handleAddModule(String data, PrintWriter out) {
	    if (modules.size() < 5) {
	        String moduleCode = data;
	        if (!isModuleAdded(moduleCode)) {
	            Module module = new Module(moduleCode);
	            modules.add(module);
	            out.println(moduleCode + " Module added successfully.");
	        } else {
	            out.println(moduleCode + " Module already added.");
	        }
	    } else {
	        out.println("Error: Maximum modules reached. You cannot add more modules.");
	    }
	}

	private static synchronized void handleRemoveModule(String data, PrintWriter out) {
	    String moduleCodeToRemove = data;
	    boolean removed = removeModule(moduleCodeToRemove);
	    if (removed) {
	        out.println((moduleCodeToRemove + " Module removed successfully."));
	    } else {
	        out.println("Error: " + moduleCodeToRemove + " Module not found.");
	    }
	}

	private static synchronized void handleAddClass(String classModuleToAdd, String classData, PrintWriter out) {
	    String[] classParts = classData.split(", ");
	    if (classParts.length == 4) { // Ensure that all parts are present
	        String day = classParts[0];
	        String start = classParts[1];
	        String end = classParts[2];
	        String room = classParts[3];

	        Module moduleToAddClass = findModule(classModuleToAdd);
	        if (moduleToAddClass != null) {
	            if (!isTimeClash(moduleToAddClass, day, start, end)) {
	                Class newClass = new Class(day, start, end, room);
	                moduleToAddClass.addClass(newClass);
	                out.println("Class added to module " + classModuleToAdd + " successfully.");
	            } else {
	                out.println("Error: This module clashes with another class: " + classModuleToAdd + ".");
	            }
	        } else {
	            out.println("Error: Module " + classModuleToAdd + " not found.");
	        }
	    } else {
	        out.println(
	            "Error: Invalid class information format. Please provide information in the format: day, start time, end time, room.");
	    }
	}

	private static synchronized void handleRemoveClass(String classModuleToRemove, String classDataToRemove, PrintWriter out) {
	    String[] classPartsToRemove = classDataToRemove.split(", ");
	    if (classPartsToRemove.length == 4) { // Ensure that all parts are present
	        String day = classPartsToRemove[0];
	        String start = classPartsToRemove[1];
	        String end = classPartsToRemove[2];
	        String room = classPartsToRemove[3];

	        Module classModuleToRemoveClass = findModule(classModuleToRemove);
	        if (classModuleToRemoveClass != null) {
	            Class classToRemove = null;
	            for (Class classObj : classModuleToRemoveClass.getClasses()) {
	                if (classObj.getDay().equals(day) && classObj.getStart().equals(start)
	                    && classObj.getEnd().equals(end) && classObj.getRoom().equals(room)) {
	                    classToRemove = classObj;
	                    break;
	                }
	            }

	            if (classToRemove != null) {
	                boolean removedClass = classModuleToRemoveClass.removeClass(classToRemove);
	                if (removedClass) {
	                    out.println("Class removed from module " + classModuleToRemove + " successfully.");
	                    out.println("Freed time slot: " + day + ", " + start + " - " + end + ", Room: " + room);
	                } else {
	                    out.println("Error: Failed to remove class from module " + classModuleToRemove);
	                }
	            } else {
	                out.println("Error: Class not found in module " + classModuleToRemove);
	            }
	        } else {
	            out.println("Error: Module " + classModuleToRemove + " not found.");
	        }
	    } else {
	        out.println(
	            "Error: Invalid class information format. Please provide information in the format: day, start time, end time, room.");
	    }
	}




	public static boolean isModuleAdded(String moduleCode) {
		for (Module module : modules) {
			if (module.getModuleCode().equals(moduleCode)) {
				return true;
			}
		}
		return false;
	}

	public static boolean removeModule(String moduleCodeToRemove) {
		for (Module module : modules) {
			if (module.getModuleCode().equals(moduleCodeToRemove)) {
				modules.remove(module);
				return true;
			}
		}
		return false;
	}

	public static Module findModule(String moduleCode) {
		for (Module module : modules) {
			if (module.getModuleCode().equals(moduleCode)) {
				return module;
			}
		}
		return null;
	}

	public static boolean isTimeClash(Module module, String day, String start, String end) {
		for (Class classObj : module.getClasses()) {
			if (classObj.getDay().equals(day)) {
				if (isOverlap(classObj.getStart(), classObj.getEnd(), start, end)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isOverlap(String start1, String end1, String start2, String end2) {
		int start1Min = parseTimeToMinutes(start1);
		int end1Min = parseTimeToMinutes(end1);
		int start2Min = parseTimeToMinutes(start2);
		int end2Min = parseTimeToMinutes(end2);

		return (start1Min < end2Min && start2Min < end1Min);
	}

	public static int parseTimeToMinutes(String time) {
		String[] parts = time.split(":");
		int hours = Integer.parseInt(parts[0]);
		int minutes = Integer.parseInt(parts[1]);
		return hours * 60 + minutes;
	}

}
