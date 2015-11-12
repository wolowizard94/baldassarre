package esc;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gnu.io.SerialPort;

/**used only for test purposes, custom routines can be created passing
 *  a list of {@link Instruction} to {@link Routine#Routine(List)} 
*/
public class ExampleRoutine extends Routine {

	public AbstractEsc esc;
	
	public ExampleRoutine() {
		super(null);
		List<Instruction> instrs = new ArrayList<>();
		instrs.add(Instruction.ARM);
		instrs.add(Instruction.START);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rpm", 2000);
		Instruction uno = new Instruction(InstructionType.SET_RPM, map); 
		instrs.add(uno);
		map = new HashMap<String, Object>();
		map.put("millis", 2000);
		Instruction sleep = new Instruction(InstructionType.SLEEP, map);
		instrs.add(sleep);
		instrs.add(Instruction.STOP);
		instrs.add(Instruction.DISARM);
		this.instructions = instrs;
	}
}
