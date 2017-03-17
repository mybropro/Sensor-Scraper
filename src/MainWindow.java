import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.StringTokenizer;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

//java simple serial connector
import jssc.*;
import java.awt.TextArea;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.DisposeEvent;

public class MainWindow {

	protected static Shell shell;
	public static String saveFile;
	public static String[] ports;
	public static SerialPort serialPort;
	public static String port;
	public static boolean record = false;
	public static String testString = "Temperature: 34.5C, PSI: 12039 psi";
	public static StyledText temperatureData1;
	public static StyledText temperatureData2;
	public static StyledText temperatureData3;
	public static StyledText PSIData1;
	public static StyledText PSIData2;
	public static StyledText flowMeterData;
	public static StyledText NOxData;
	public static StyledText SOxData;
	public static StyledText COData;
	public static StyledText CO2Data;
	public static StyledText O2Data;
	public static StyledText UFMPData;
	public static Label lblInfo;

	public static double flowMultiplier = 1.0;

	public static String result = "Temp1: 20 C, Temp2: 30 C, Temp3: 33 C, Flow Rate: 40 l/s, Pressure1: 50 psi, Pressure2: 60 psi, NOX: 70 ppm, SOX: 80 ppm, CO: 90 ppm, CO2: 100 %, O2: 110 %, UFMP: 120 p/cm^3 x 10^6";

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */

	public static void main(String[] args) {
		MainWindow window = new MainWindow();
		window.manageMetaData();
		refreshPorts();

		// by default have selected port be the first port available

		// establish inital connection

		// pass array of most recent data into spreadSheet
		// offer the users a list of com ports in the com ports menu
		try {
			// opens gui, following code in try isn't executed until the window
			// is closed?
			window.open();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {

		shell = new Shell();
		shell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent arg0) {
				if (record) {
					try {
						serialPort.closePort();
					} catch (SerialPortException e) {
						e.printStackTrace();
					}
				}
				System.exit(0);
			}
		});
		shell.setSize(267, 459);
		shell.setText("Sensor Readout Panel");
		shell.setImage(
				SWTResourceManager.getImage(MainWindow.class, "/javax/swing/plaf/metal/icons/ocean/computer.gif"));

		// Temperature 1
		Label lblTemperature1 = new Label(shell, SWT.NONE);
		lblTemperature1.setBounds(16, 10, 76, 16);

		lblTemperature1.setText("Temperature 1:");
		temperatureData1 = new StyledText(shell, SWT.BORDER);
		temperatureData1.setEditable(false);
		temperatureData1.setText("NoData");
		temperatureData1.setBounds(166, 10, 69, 19);
		// Temperature 2
		Label lblTemperature2 = new Label(shell, SWT.NONE);
		lblTemperature2.setBounds(16, 32, 76, 16);
		lblTemperature2.setText("Temperature 2:");
		temperatureData2 = new StyledText(shell, SWT.BORDER);
		temperatureData2.setEditable(false);
		temperatureData2.setText("NoData");
		temperatureData2.setBounds(166, 32, 69, 19);
		// Temperature 3
		Label lblTemperature3 = new Label(shell, SWT.NONE);
		lblTemperature3.setBounds(16, 54, 76, 16);
		lblTemperature3.setText("Temperature 3:");
		temperatureData3 = new StyledText(shell, SWT.BORDER);
		temperatureData3.setEditable(false);
		temperatureData3.setText("NoData");
		temperatureData3.setBounds(166, 54, 69, 19);
		Label label = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(10, 78, 230, 2);

		// PSI 1
		Label lblPSI1 = new Label(shell, SWT.NONE);
		lblPSI1.setBounds(16, 85, 56, 16);
		lblPSI1.setText("PSI 1:");
		PSIData1 = new StyledText(shell, SWT.BORDER);
		PSIData1.setEditable(false);
		PSIData1.setText("NoData");
		PSIData1.setBounds(166, 85, 69, 19);
		// PSI 2
		Label lblPSI2 = new Label(shell, SWT.NONE);
		lblPSI2.setBounds(16, 107, 56, 16);
		lblPSI2.setText("PSI 2:");
		PSIData2 = new StyledText(shell, SWT.BORDER);
		PSIData2.setEditable(false);
		PSIData2.setText("NoData");
		PSIData2.setBounds(166, 107, 69, 19);
		Label label_2 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_2.setBounds(10, 131, 230, 2);

		// FlowMeter
		Label lblFlowMeter = new Label(shell, SWT.NONE);
		lblFlowMeter.setBounds(16, 138, 56, 16);
		lblFlowMeter.setText("Flow: ");
		flowMeterData = new StyledText(shell, SWT.BORDER);
		flowMeterData.setEditable(false);
		flowMeterData.setText("NoData");
		flowMeterData.setBounds(166, 138, 69, 19);
		Label label_1 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_1.setBounds(10, 162, 230, 2);

		// NOx
		Label lblPSI_1 = new Label(shell, SWT.NONE);
		lblPSI_1.setBounds(16, 169, 56, 16);
		lblPSI_1.setText("NOx%:");
		StyledText NOxData = new StyledText(shell, SWT.BORDER);
		NOxData.setEditable(false);
		NOxData.setText("NoData");
		NOxData.setBounds(166, 169, 69, 19);
		Label label_3 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_3.setBounds(10, 193, 230, 2);

		// SOx
		Label lblSox = new Label(shell, SWT.NONE);
		lblSox.setBounds(16, 200, 56, 16);
		lblSox.setText("SOx%:");
		StyledText SOxData = new StyledText(shell, SWT.BORDER);
		SOxData.setEditable(false);
		SOxData.setText("NoData");
		SOxData.setBounds(166, 200, 69, 19);
		Label label_4 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_4.setBounds(10, 224, 230, 2);

		// CO
		Label lblCO = new Label(shell, SWT.NONE);
		lblCO.setBounds(16, 231, 56, 16);
		lblCO.setText("CO%:");
		StyledText COData = new StyledText(shell, SWT.BORDER);
		COData.setEditable(false);
		COData.setText("NoData");
		COData.setBounds(166, 231, 69, 19);
		Label label_5 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_5.setBounds(10, 255, 230, 2);

		// CO2
		Label lblCO2 = new Label(shell, SWT.NONE);
		lblCO2.setBounds(16, 262, 56, 16);
		lblCO2.setText("CO2%:");
		StyledText CO2Data = new StyledText(shell, SWT.BORDER);
		CO2Data.setEditable(false);
		CO2Data.setText("NoData");
		CO2Data.setBounds(166, 262, 69, 19);
		Label label_6 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_6.setBounds(10, 284, 230, 2);

		// O2
		Label lblO2 = new Label(shell, SWT.NONE);
		lblO2.setBounds(16, 293, 56, 16);
		lblO2.setText("O2%:");
		StyledText O2Data = new StyledText(shell, SWT.BORDER);
		O2Data.setEditable(false);
		O2Data.setText("NoData");
		O2Data.setBounds(166, 293, 69, 19);
		Label label_7 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_7.setBounds(10, 315, 230, 2);

		// UFMP
		Label lblUFMP = new Label(shell, SWT.NONE);
		lblUFMP.setBounds(16, 324, 56, 16);
		lblUFMP.setText("UFMP%:");
		StyledText UFMPData = new StyledText(shell, SWT.BORDER);
		UFMPData.setEditable(false);
		UFMPData.setText("NoData");
		UFMPData.setBounds(166, 324, 69, 19);
		Label label_8 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_8.setBounds(10, 346, 230, 2);

		// Start/STOP buttons
		Button btnStartCollection = new Button(shell, SWT.NONE);
		btnStartCollection.setBounds(16, 358, 92, 21);
		btnStartCollection.setText("Start Collection");

		Button btnStopCollection = new Button(shell, SWT.NONE);
		btnStopCollection.setBounds(143, 358, 92, 21);
		btnStopCollection.setText("Stop Collection");

		// informational text
		lblInfo = new Label(shell, SWT.NONE);
		lblInfo.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.NORMAL));
		lblInfo.setAlignment(SWT.RIGHT);
		lblInfo.setBounds(12, 383, 237, 16);
		lblInfo.setText("Ready");

		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		MenuItem mntmNewSubmenu = new MenuItem(menu, SWT.CASCADE);
		mntmNewSubmenu.setText("Options");

		Menu menu_1 = new Menu(mntmNewSubmenu);
		mntmNewSubmenu.setMenu(menu_1);

		MenuItem mntmComPorts = new MenuItem(menu_1, SWT.CASCADE);
		mntmComPorts.setText("COM Ports");

		Menu menu_2 = new Menu(mntmComPorts);
		mntmComPorts.setMenu(menu_2);

		MenuItem mntmCom1 = new MenuItem(menu_2, SWT.CHECK);

		mntmCom1.setSelection(true);
		mntmCom1.setText("COM1");

		MenuItem mntmCom2 = new MenuItem(menu_2, SWT.CHECK);
		mntmCom2.setText("COM2");

		if (ports.length == 1) {
			mntmCom1.setText(ports[0]);
			mntmCom2.setEnabled(false);
		}

		MenuItem mntmRefreshPorts = new MenuItem(menu_2, SWT.NONE);
		mntmRefreshPorts.setToolTipText("Fetches all ports currently plugged in");
		mntmRefreshPorts.setText("Refresh Ports");

		MenuItem mntmFlowRadius = new MenuItem(menu_1, SWT.CASCADE);
		mntmFlowRadius.setText("Flow Radius");

		Menu menu_3 = new Menu(mntmFlowRadius);
		mntmFlowRadius.setMenu(menu_3);

		MenuItem mntmcm20cm = new MenuItem(menu_3, SWT.NONE);
		mntmcm20cm.setText("2.0cm");

		MenuItem mntmcm25cm = new MenuItem(menu_3, SWT.NONE);
		mntmcm25cm.setText("2.5cm");

		MenuItem mntmcm30cm = new MenuItem(menu_3, SWT.NONE);
		mntmcm30cm.setText("3.0cm");

		MenuItem mntmcm40cm = new MenuItem(menu_3, SWT.NONE);
		mntmcm40cm.setText("4.0cm");

		MenuItem mntmViewOutputCsv = new MenuItem(menu_1, SWT.NONE);
		mntmViewOutputCsv.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// SHOW FILE IN FILE EXPLORER
				try {
					Runtime.getRuntime().exec("explorer.exe /select," + saveFile);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		mntmViewOutputCsv.setToolTipText("open file location");
		mntmViewOutputCsv.setText("View Output CSV");

		MenuItem mntmChangeSave = new MenuItem(menu_1, SWT.CASCADE);
		mntmChangeSave.setToolTipText("Define a new file to save to");
		mntmChangeSave.setText("Change File Save Location");

		// button clicks
		btnStartCollection.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// serial output a 1. should receive a serial 49
				if (!record) {// make sure we're not already recording
					serialPort = new SerialPort(mntmCom1.getText());
					try {
						serialPort.openPort();// Open port
						serialPort.setParams(9600, 8, 1, 0);// Set params
						int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;// Prepare
						serialPort.setEventsMask(mask);// Set mask
						serialPort.addEventListener(new SerialPortReader());// Add
						serialPort.writeBytes("1".getBytes());// mask

						// SerialPortEventListener
					} catch (SerialPortException ex) {
						shell.getDisplay().asyncExec(new Runnable() {

							@Override
							public void run() {
								lblInfo.setText(ex.getPortName() + " cannot be found");
							}
						});
					}
				}
			}
		});

		btnStopCollection.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// serial output a 0. should receive a serial 48
				try {
					serialPort.writeBytes("0".getBytes());
				} catch (SerialPortException e1) {
					e1.printStackTrace();
				}

			}
		});

		// COM Ports menu buttons
		mntmCom1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				mntmCom2.setSelection(false);
			}
		});
		mntmCom2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				mntmCom1.setSelection(false);
			}
		});

		// refresh ports button in the COM menu. Relists all the available ports
		mntmRefreshPorts.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshPorts();
				if (ports.length == 0) {
					mntmCom1.setEnabled(false);
					mntmCom2.setEnabled(false);
				}
				if (ports.length == 1) {
					mntmCom1.setEnabled(true);
					mntmCom1.setText(ports[0]);
					mntmCom2.setEnabled(false);
				}
			}
		});

		mntmcm20cm.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				flowMultiplier = 0.250;
				mntmcm25cm.setSelection(false);
				mntmcm30cm.setSelection(false);
				mntmcm40cm.setSelection(false);
			}
		});
		mntmcm25cm.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				flowMultiplier = 0.391;
				mntmcm20cm.setSelection(false);
				mntmcm30cm.setSelection(false);
				mntmcm40cm.setSelection(false);
			}
		});
		mntmcm30cm.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				flowMultiplier = 0.563;
				mntmcm20cm.setSelection(false);
				mntmcm25cm.setSelection(false);
				mntmcm40cm.setSelection(false);
			}
		});
		mntmcm40cm.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				flowMultiplier = 1.0;
				mntmcm20cm.setSelection(false);
				mntmcm25cm.setSelection(false);
				mntmcm30cm.setSelection(false);
			}
		});

		mntmChangeSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File(""));
				int retrival = chooser.showSaveDialog(null);
				if (retrival == JFileChooser.APPROVE_OPTION) {
					try {
						// Make new save File Location
						saveFile = chooser.getSelectedFile().getAbsolutePath();
						File newSave = new File(saveFile);
						manageMetaData(newSave);
						// tells teensy to start outputting data
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});
	}

	public void manageMetaData() {
		// check if metadata file exists, if not create one;
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			File metadataFile = new File("metadata.txt");
			// if file doesnt exists, then create it
			if (!metadataFile.exists()) {
				metadataFile.createNewFile();
				System.out.println("metadata.txt did not exist, so it was created.");

				fw = new FileWriter(metadataFile.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write("data.csv");
				bw.close();
			}
			BufferedReader reader = new BufferedReader(new FileReader(metadataFile));
			saveFile = reader.readLine();

			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void manageMetaData(File newSaveLoc) {
		// check if metadata file exists, if not create one;
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			File metadataFile = new File("metadata.txt");
			// if file doesnt exists, then create it
			if (!metadataFile.exists()) {
				metadataFile.createNewFile();
				System.out.println("metadata.txt did not exist, so it was created.");

				fw = new FileWriter(metadataFile.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(newSaveLoc.getAbsolutePath());
				bw.close();
			} else {
				fw = new FileWriter(metadataFile.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(newSaveLoc.getAbsolutePath());
				bw.close();
			}
			BufferedReader reader = new BufferedReader(new FileReader(metadataFile));
			saveFile = reader.readLine();
			reader.close();
			shell.getDisplay().asyncExec(new Runnable() {

				@Override
				public void run() {
					lblInfo.setText("Data file is located at " + saveFile.toString());
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// if function is called mid process, it will always check if file exists.
	// This might seem counter
	// intuitaive because it's checking every second for a CSV file, but incase
	// its located on an external drive
	// or somewhere where it's unstable, a check of the file is a good idea.
	public static void putInSpreadsheet(String[] data) {
		StringBuilder sb = new StringBuilder();
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			File file = new File(saveFile);
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
				sb.append("Time,");
				sb.append("Temp1,");
				sb.append("Temp2,");
				sb.append("Temp3,");
				sb.append("PSI1,");
				sb.append("PSI2,");
				sb.append("Flow,");
				sb.append("N0x,");
				sb.append("SOx,");
				sb.append("CO,");
				sb.append("CO2,");
				sb.append("O2,");
				sb.append("UFMP");
				sb.append('\n');
			}

			fw = new FileWriter(file.getAbsoluteFile(), true);
			// true = append file
			bw = new BufferedWriter(fw);

			Date time = new Date();
			sb.append(time.getTime());
			sb.append(',');
			sb.append(data[0]);// TEMP1
			sb.append(',');
			sb.append(data[1]);// TEMP2
			sb.append(',');
			sb.append(data[2]);// TEMP3
			sb.append(',');
			sb.append(data[3]);// PSI1
			sb.append(',');
			sb.append(data[4]);// PSI2
			sb.append(',');
			sb.append(data[5]);// Flow
			sb.append(',');
			sb.append(data[6]);// NOx
			sb.append(',');
			sb.append(data[7]);// SOx
			sb.append(',');
			sb.append(data[8]);// CO
			sb.append(',');
			sb.append(data[9]);// CO2
			sb.append(',');
			sb.append(data[10]);// O2
			sb.append(',');
			sb.append(data[11]);// UFMP
			sb.append('\n');

			String line = sb.toString();
			bw.write(line);
			// System.out.println(line);

		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			// clear out the buffer and file writer
			try {
				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();
				//

				// temperatureData.setText(newLabels[0]);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static String[] getPorts() {
		String[] portNames = SerialPortList.getPortNames();
		return portNames;
	}

	public static void refreshPorts() {
		ports = getPorts();
		for (int i = 0; i < ports.length; i++) {
			System.out.println("Port " + i + ": " + ports[i]);
		}
	}

	static class SerialPortReader implements SerialPortEventListener {

		public void serialEvent(SerialPortEvent event) {
			if (event.isRXCHAR()) {
				try {
					// here's where it should split the file and then pass
					// it into the spreadsheet func.
					String line = serialPort.readString();
					// now we have to split the input, form an array and
					// feed it into spreadsheet
					// System.out.println("Recieved a " + line);
					if (line.contains("STOP")) {// received after sending a 0
						System.out.println("Recieved a 0. Program has stopped recording data");
						shell.getDisplay().asyncExec(new Runnable() {

							@Override
							public void run() {
								lblInfo.setText("Recording stopped");
							}
						});
						record = false;
						serialPort.removeEventListener();
						serialPort.setEventsMask(0);
						serialPort.closePort();
					}
					if (line.contains("START")) {// received after sending a 1
						System.out.println("Recieved a 1. Program has started recording data");
						shell.getDisplay().asyncExec(new Runnable() {

							@Override
							public void run() {
								lblInfo.setText("Recording");
							}
						});

						record = true;
					} else {

						if (record) {
							// now split into various measurments in data
							String[] data = processData(line);

							putInSpreadsheet(data);
						}
					}

				} catch (SerialPortException ex) {
					System.out.println(ex);
				}
			}

			// cases that won't happen in everyday use(hopefully)
			else if (event.isCTS()) {// If CTS line has changed state
				if (event.getEventValue() == 1) {// If line is ON
					System.out.println("CTS - ON");
				} else {
					System.out.println("CTS - OFF");
				}
			} else if (event.isDSR()) {/// If DSR line has changed state
				if (event.getEventValue() == 1) {// If line is ON
					System.out.println("DSR - ON");
				} else {
					System.out.println("DSR - OFF");
				}
			}
		}
	}

	public static String[] processData(String line) {
		String delims = "[ ,]+";
		// System.out.println(line);
		String[] tokens = line.split(delims);
		for (int i = 0; i < tokens.length; i++) {
			tokens[i] = tokens[i].replaceAll("[^\\d.]", "");
			// System.out.println("Token " + i + ": " + tokens[i]);
		}

		// need different method if im using multiple COM
		// ports
		// probably have 2 events that feed into one method
		// that checks if the data is unique

		String[] data = new String[12];
		data[0] = tokens[1];// temp1
		data[1] = tokens[4];// temp2
		data[2] = tokens[7];// temp3
		data[3] = Double.toString(Double.parseDouble(tokens[11]) * flowMultiplier);// Flow
		data[4] = tokens[14];// PSI1
		data[5] = tokens[17];// PSI2
		data[6] = tokens[20];// NOx
		data[7] = tokens[23];// SOx
		data[8] = tokens[26];// CO
		data[9] = tokens[29];// CO2
		data[10] = tokens[32];// O2
		data[11] = tokens[35];// UFMP

		/*
		 * for (int i = 0; i < data.length; i++) { System.out.println("DATA " +
		 * i + ": " + data[i]); }
		 */
		shell.getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				temperatureData1.setText(data[0]);
				temperatureData2.setText(data[1]);
				temperatureData3.setText(data[2]);
				flowMeterData.setText(data[3]);
				PSIData1.setText(data[4]);
				PSIData2.setText(data[5]);
				NOxData.setText(data[6]);
				SOxData.setText(data[7]);
				COData.setText(data[8]);
				CO2Data.setText(data[9]);
				O2Data.setText(data[10]);
				UFMPData.setText(data[9]);
			}
		});

		return data;
	}

	private String calculate(String s) {
		// count how many numbers are in the string
		StringTokenizer q = new StringTokenizer(s, "/*-+", false);
		if (q.countTokens() == 2) {
			// if it's just two, calculate the answer
			StringTokenizer t = new StringTokenizer(s, "/*-+", true);
			String v1 = t.nextToken();
			// check for negative signs
			if (v1.equals("-"))
				v1 += t.nextToken();
			String f = t.nextToken();
			String v2 = t.nextToken();
			if (v2.equals("-"))
				v2 += t.nextToken();
			double result = 0;
			switch (f) {
			case "+":
				result = Double.parseDouble(v1) + Double.parseDouble(v2);
				break;
			case "-":
				result = Double.parseDouble(v1) - Double.parseDouble(v2);
				break;
			case "*":
				result = Double.parseDouble(v1) * Double.parseDouble(v2);
				break;
			case "/":
				if (Double.parseDouble(v2) == 0)
					return "div by 0";
				else
					result = Double.parseDouble(v1) / Double.parseDouble(v2);
				break;
			}
			return String.valueOf(result);
		} else {
			// if it's multiple, divide the string up,
			StringTokenizer t = new StringTokenizer(s, "/*-+", true);
			ArrayList<String> a = new ArrayList<String>();
			String p = t.nextToken();
			if (p.equals("-"))
				p += t.nextToken();
			a.add(p);
			// turn it into an arraylist with negatives included
			while (t.hasMoreTokens()) {
				a.add(t.nextToken());
				String r = t.nextToken();
				if (r.equals("-"))
					r += t.nextToken();
				a.add(r);
			}
			// cycle through each function, calculating each set of numbers and
			// replacing it with the
			// answer
			while (a.contains("/")) {
				int i = a.indexOf("/");
				String result = calculate(a.get(i - 1) + a.get(i) + a.get(i + 1));
				a.set(i, result);
				a.remove(i + 1);
				a.remove(i - 1);
			}
			while (a.contains("*")) {
				int i = a.indexOf("*");
				String result = calculate(a.get(i - 1) + a.get(i) + a.get(i + 1));
				a.set(i, result);
				a.remove(i + 1);
				a.remove(i - 1);
			}
			while (a.contains("-")) {
				int i = a.indexOf("-");
				String result = calculate(a.get(i - 1) + a.get(i) + a.get(i + 1));
				a.set(i, result);
				a.remove(i + 1);
				a.remove(i - 1);
			}
			while (a.contains("+")) {
				int i = a.indexOf("+");
				String result = calculate(a.get(i - 1) + a.get(i) + a.get(i + 1));
				a.set(i, result);
				a.remove(i + 1);
				a.remove(i - 1);
			}
			return a.get(0);
		}
	}
}
