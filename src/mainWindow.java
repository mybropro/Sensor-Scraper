import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.graphics.Point;

public class mainWindow {

	protected Shell shlSensorReadoutPanel;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			mainWindow window = new mainWindow();
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
		shlSensorReadoutPanel.open();
		shlSensorReadoutPanel.layout();
		while (!shlSensorReadoutPanel.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlSensorReadoutPanel = new Shell();
		shlSensorReadoutPanel.setSize(261, 222);
		shlSensorReadoutPanel.setText("Sensor Readout Panel");
		
		Label lblTemperature = new Label(shlSensorReadoutPanel, SWT.NONE);
		lblTemperature.setBounds(0, 10, 76, 16);
		lblTemperature.setText("Temperature:");
		
		Label lblRpm = new Label(shlSensorReadoutPanel, SWT.NONE);
		lblRpm.setBounds(0, 32, 56, 16);
		lblRpm.setText("RPM:");
		
		Label lblRpm_1 = new Label(shlSensorReadoutPanel, SWT.NONE);
		lblRpm_1.setBounds(0, 54, 56, 16);
		lblRpm_1.setText("NOx%:");
		
		Label lblSox = new Label(shlSensorReadoutPanel, SWT.NONE);
		lblSox.setBounds(0, 76, 56, 16);
		lblSox.setText("SOx%:");
		
		Button btnStartCollection = new Button(shlSensorReadoutPanel, SWT.NONE);
		btnStartCollection.setBounds(0, 98, 92, 21);
		btnStartCollection.setText("Start Collection");
		
		Button btnStopCollection = new Button(shlSensorReadoutPanel, SWT.NONE);
		btnStopCollection.setBounds(98, 98, 92, 21);
		btnStopCollection.setText("Stop Collection");
		
		ProgressBar progressBar = new ProgressBar(shlSensorReadoutPanel, SWT.NONE);
		progressBar.setBounds(0, 148, 170, 17);
		
		Label lblFileDumpProgress = new Label(shlSensorReadoutPanel, SWT.NONE);
		lblFileDumpProgress.setBounds(0, 126, 114, 16);
		lblFileDumpProgress.setText("File Dump Progress:");
		
		Menu menu = new Menu(shlSensorReadoutPanel, SWT.BAR);
		shlSensorReadoutPanel.setMenuBar(menu);
		
		MenuItem mntmNewSubmenu = new MenuItem(menu, SWT.CASCADE);
		mntmNewSubmenu.setText("New SubMenu");
		
		Menu menu_1 = new Menu(mntmNewSubmenu);
		mntmNewSubmenu.setMenu(menu_1);

	}
}
