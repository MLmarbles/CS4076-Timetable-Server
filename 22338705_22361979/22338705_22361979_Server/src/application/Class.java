package application;

public class Class {
	private String day;
	private String start;
	private String end;
	private String room;

	public Class(String day, String start, String end, String room) {
		this.day = day;
		this.start = start;
		this.end = end;
		this.room = room;
	}

	public String getDay() {
		return day;
	}

	public String getStart() {
		return start;
	}
	public void setStart(String time) {
		this.start = time;
	}

	public String getEnd() {
		return end;
	}
	public void setEnd(String time) {
		this.end = time;
	}

	public String getRoom() {
		return room;
	}

	@Override
	public String toString() {
		return "Day: " + day + ", Time: " + start + " - " + end + ", Room: " + room;
	}

	// Custom serialization method
	public String serialize() {
		StringBuilder sb = new StringBuilder();
		sb.append(day).append(";") // Append day
				.append(start).append(";") // Append start time
				.append(end).append(";") // Append end time
				.append(room).append(";"); // Append room

		return sb.toString();
	}

	// Custom deserialization method
	public static Class deserialize(String data) {
		String[] parts = data.split(";");
		String day = parts[0];
		String start = parts[1];
		String end = parts[2];
		String room = parts[3];

		return new Class(day, start, end, room);
	}
}
