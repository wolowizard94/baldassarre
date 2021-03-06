package routine;

import java.util.HashMap;
import java.util.Map;

import esc.AbstractEsc;

/**
 * Factory for the instructions to send to an {@link AbstractEsc}. Some calls
 * for new instructions actually instantiate a new {@link Instruction}, others
 * simply reuse previously objects
 * 
 * @author federico
 *
 */
public class Instruction {

	private static final Instruction ARM = new Instruction(InstructionType.ARM,
			null);
	private static final Instruction DISARM = new Instruction(
			InstructionType.DISARM, null);
	private static final Instruction START = new Instruction(
			InstructionType.START, null);
	private static final Instruction STOP = new Instruction(
			InstructionType.STOP, null);
	private static final Instruction STOP_TELEMETRY = new Instruction(
			InstructionType.STOP_TELEMETRY, null);

	public static final Instruction newAcceleration(int from, int to,
			double pace) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("from", from);
		map.put("to", to);
		map.put("pace", pace);
		return new Instruction(InstructionType.ACCELERATE, map);
	}

	public static final Instruction newArm() {
		return ARM;
	}

	public static final Instruction newDirection(String direction) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("direction", direction);
		return new Instruction(InstructionType.DIRECTION, map);
	}

	public static final Instruction newDisarm() {
		return DISARM;
	}

	public static final Instruction newSetRpm(int rpm) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rpm", rpm);
		return new Instruction(InstructionType.SET_RPM, map);
	}

	public static final Instruction newSleep(long millis) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("millis", millis);
		return new Instruction(InstructionType.SLEEP, map);
	}

	public static final Instruction newStart() {
		return START;
	}

	public static final Instruction newStartTelemetry(int frequency) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("frequency", frequency);
		return new Instruction(InstructionType.START_TELEMETRY, map);
	}

	public static final Instruction newStop() {
		return STOP;
	}

	public static final Instruction newStopTelemetry() {
		return STOP_TELEMETRY;
	}

	public final InstructionType type;
	public final Map<String, Object> parameters;

	private Instruction(InstructionType type, Map<String, Object> parameters) {
		this.type = type;
		this.parameters = parameters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Instruction other = (Instruction) obj;
		if (type != other.type)
			return false;
		if (parameters == null) {
			if (other.parameters != null)
				return false;
		} else if (!parameters.equals(other.parameters))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (parameters == null ? 0 : parameters.hashCode());
		result = prime * result + (type == null ? 0 : type.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return type.toString() + " " + (parameters == null ? "" : parameters);
	}
}
