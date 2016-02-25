package routine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import esc.AbstractEsc;
import esc.TelemetryParameter;

/**
 * Used only for test purposes, custom routines can be created passing a list of
 * {@link Instruction} to the constructor of {@link Routine#Routine(List)}, even
 * if the best way to define a routine is by creating a .rou file that can be
 * parsed through a {@link RoutineLoader}
 */
@Deprecated
public class ConstantSpeedRoutine extends Routine {

	public AbstractEsc esc;

	public ConstantSpeedRoutine() {
		super("Velocità costante 2000", Arrays.asList(TelemetryParameter.values()), new ArrayList<Instruction>());
		List<Instruction> instrs = new ArrayList<>();
		instrs.add(Instruction.ARM);
		instrs.add(Instruction.START);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rpm", 2000);
		Instruction uno = new Instruction(InstructionType.SET_RPM, map);
		instrs.add(uno);
		map = new HashMap<String, Object>();
		map.put("millis", 10000L);
		Instruction sleep = new Instruction(InstructionType.SLEEP, map);
		instrs.add(sleep);
		instrs.add(Instruction.STOP);
		instrs.add(Instruction.DISARM);
		this.instructions = instrs;
	}
}