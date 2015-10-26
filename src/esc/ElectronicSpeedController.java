package esc;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;

import gnu.io.SerialPort;

public class ElectronicSpeedController {
	private InputStream input;
	private OutputStream output;
	private ReaderThread reader;
	private static HashSet<String> telemetryParameters = new HashSet<>();
	public static String[] allParameters = {"INPUT MODE", "RUN MODE", "ESC STATE", "PERCENT IDLE", "COMM PERIOD", "BAD DETECTS", "FET DUTY", "RPM", "AMPS AVG", "AMPS MAX", "BAT VOLTS", "MOTOR VOLTS", "DISARM CODE", "CAN NET ID"};

	public ElectronicSpeedController(SerialPort port) throws IOException {
		this.input = port.getInputStream();
		this.output = port.getOutputStream();
		ElectronicSpeedController.telemetryParameters.addAll(Arrays.asList(allParameters));
	}
	
	public ElectronicSpeedController sleep(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public ElectronicSpeedController setRPM(int rpm) {
		return sendCommand("rpm " + rpm);
	}
	
	public ElectronicSpeedController arm() {
		return sendCommand("arm");
	}
	
	public ElectronicSpeedController disarm() {
		return sendCommand("disarm");
	}
	
	public ElectronicSpeedController start() {
		return sendCommand("start");
	}
	
	public ElectronicSpeedController stop() {
		return sendCommand("stop");
	}
	
	public ElectronicSpeedController sendCommand(String command){
		try {
			// l'ESC usa la codifica UTF-8 e ha bisogno dei caratteri di LF e CR
			output.write(command.trim().getBytes("UTF-8"));
			output.write(13); // LF
			output.write(10); // CR
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public ElectronicSpeedController startTelemetry(int frequency, PrintWriter writer){
		sendCommand("telemetry " + frequency);
		if (reader != null) {
			reader = new ReaderThread(writer);
			reader.start();
		} else {
			reader.setOutput(writer);
			if (!reader.isAlive()){
				reader.start();
			}
		}
		return this;
	}
	
	public ElectronicSpeedController stopTelemetry() {
		sendCommand("telemetry 0");
		if (reader != null) {
			reader.interrupt();
		}
		return this;
	}

	public void addTelemetryParameter(String parameter){
		telemetryParameters.add(parameter);
	}
	
	public void addTelemetryParameters(String[] parameters){
		telemetryParameters.addAll(Arrays.asList(parameters));
	}
	
	public void removeTelemetryParameter(String parameter){
		telemetryParameters.remove(parameter);
	}
	
	private class ReaderThread extends Thread {
		private PrintWriter writer;
		private ByteArrayOutputStream inputBuffer;
		public ReaderThread(PrintWriter writer){
			this.writer = writer;
			this.inputBuffer = new ByteArrayOutputStream();
		}
		public void setOutput(PrintWriter writer){
			this.writer = writer;
		}
		public void run() {
			try {
				byte singleData;
				while ((singleData = (byte) input.read()) != -1) {
					inputBuffer.write(singleData);
					if (singleData == 13) {
						ByteArrayInputStream bin = new ByteArrayInputStream(inputBuffer.toByteArray());
						BufferedReader reader = new BufferedReader(new InputStreamReader(bin, "UTF-8"));
						String line = reader.readLine();
						for (String parameter : telemetryParameters) {
							if (line.startsWith(parameter))
								writer.write(line);
						}
						inputBuffer.reset();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}