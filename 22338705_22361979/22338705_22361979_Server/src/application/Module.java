package application;

import java.util.ArrayList;
import java.util.List;

public class Module {
	private String moduleCode;
	private ArrayList<Class> classes;
	private int moduleId;

	public Module(String moduleCode) {
		this.moduleCode = moduleCode;
		this.classes = new ArrayList<>();
	}

	// Getter and setter for module code
	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public int getModuleId() {
		return moduleId;
	}

	public void setModuleId(int id) {
		this.moduleId = id;
	}

	// Getter and setter for classes
	public ArrayList<Class> getClasses() {
		return classes;
	}

	public void setClasses(ArrayList<Class> classes) {
		this.classes = classes;
	}

	// Add a class to the module
	public void addClass(Class classObj) {
		this.classes.add(classObj);
	}

	public boolean removeClass(Class classObj) {
		return this.classes.remove(classObj);
	}

	// Custom serialization method
	public String serialize() {
		StringBuilder sb = new StringBuilder();
		sb.append(moduleCode).append(";"); // Append module code

		for (Class classObj : classes) {
			sb.append(classObj.serialize()).append(":"); // Append serialized class
		}

		return sb.toString();
	}

	// Custom deserialization method
	public static Module deserialize(String data) {
		String[] parts = data.split(";");
		String moduleCode = parts[0];
		Module module = new Module(moduleCode);

		if (parts.length > 1) {
			String[] classData = parts[1].split(":");
			for (String classStr : classData) {
				Class classObj = Class.deserialize(classStr); // Deserialize class
				module.addClass(classObj);
			}
		}

		return module;
	}

}
