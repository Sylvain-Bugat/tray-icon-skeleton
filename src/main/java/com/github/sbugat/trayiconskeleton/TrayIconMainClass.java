package com.github.sbugat.trayiconskeleton;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Main class of the Tray icon executable jar
 *
 * @author Sylvain Bugat
 *
 */
public class TrayIconMainClass {

	private static final String TRAY_ICON_SKELETON_PROJECT_URL = "https://github.com/Sylvain-Bugat/tray-icon-skeleton";

	/** Load the tray icon image to display in the tray bar*/
	private static final Image TRAY_ICON_IMAGE = Toolkit.getDefaultToolkit().getImage( TrayIconMainClass.class.getClassLoader().getResource( "TrayIcon.png" ) ); //$NON-NLS-1$

	/**
	 * Main program launched in the jar file
	 *
	 * @param args
	 * @throws IOException
	 */
	public static void main( final String args[] ) throws IOException{

		//Test if the system support the system tray
		if( SystemTray.isSupported() ) {

			//Try to use the system Look&Feel
			try {
				UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
			}
			catch( final ClassNotFoundException exception ) {
				//If System Look&Feel is not supported, stay with the default one
			}
			catch( final InstantiationException exception ) {
				//If System Look&Feel is not supported, stay with the default one
			}
			catch( final IllegalAccessException exception ) {
				//If System Look&Feel is not supported, stay with the default one
			}
			catch( final UnsupportedLookAndFeelException exception ) {
				//If System Look&Feel is not supported, stay with the default one
			}

			//Add the icon  to the system tray
			final TrayIcon trayIcon = new TrayIcon( TRAY_ICON_IMAGE, "Tray icon skeleton" );
			trayIcon.setImageAutoSize( true );

			// Get the system default browser to open execution details
			final Desktop desktop = Desktop.getDesktop();

			//Action listener to get click on top menu items
			final ActionListener menuListener = new ActionListener() {
				@SuppressWarnings("synthetic-access")
				public void actionPerformed( final ActionEvent e) {

					if( JMenuItem.class.isInstance( e.getSource() ) ){

						JMenuItem jMenuItem = (JMenuItem) e.getSource();
						JOptionPane.showMessageDialog( null, "It works, you clicked on:" + System.lineSeparator() + jMenuItem.getText(), "Your skill is great!!", JOptionPane.INFORMATION_MESSAGE ); //$NON-NLS-1$
					}
				}
			};

			//About menu listener
			final ActionListener aboutListener = new ActionListener() {
				@SuppressWarnings("synthetic-access")
				public void actionPerformed( final ActionEvent e) {

					//Open an URL using the system default browser
					try {
						final URI executionURI = new URI( TRAY_ICON_SKELETON_PROJECT_URL );
						desktop.browse( executionURI );
					}
					catch( final URISyntaxException exception ) {

						final StringWriter stringWriter = new StringWriter();
						exception.printStackTrace( new PrintWriter( stringWriter ) );
						JOptionPane.showMessageDialog( null, exception.getMessage() + System.lineSeparator() + stringWriter.toString(), "Tray icon skeleton redirection error", JOptionPane.ERROR_MESSAGE ); //$NON-NLS-1$
					}
					catch( final IOException exception ) {

						final StringWriter stringWriter = new StringWriter();

						exception.printStackTrace( new PrintWriter( stringWriter ) );
						JOptionPane.showMessageDialog( null, exception.getMessage() + System.lineSeparator() + stringWriter.toString(), "Tray icon skeleton redirection error", JOptionPane.ERROR_MESSAGE ); //$NON-NLS-1$
					}
				}
			};

			//Get the system tray
			final SystemTray tray = SystemTray.getSystemTray();

			//Tray icon skeleton exit listener
			final ActionListener exitListener = new ActionListener() {
				@SuppressWarnings("synthetic-access")
				public void actionPerformed( final ActionEvent e) {
					//Important: remove the icon from the tray to dispose it
					tray.remove( trayIcon );
					System.exit( 0 );
				}
			};

			//Popup menu
			JPopupMenu.setDefaultLightWeightPopupEnabled( true );
			final JPopupMenu popupMenu = new JPopupMenu();

			//Add 10 menu items
			for( int i = 0 ; i < 10 ; i++ ){

				final JMenuItem jMenuItem = new JMenuItem( "menu item " + i );
				popupMenu.add( jMenuItem );
				jMenuItem.addActionListener( menuListener );
			}

			//Adding some menu separator
			popupMenu.addSeparator();

			final JMenuItem aboutItem = new JMenuItem( "About Tray icon skeleton" ); //$NON-NLS-1$
			popupMenu.add( aboutItem );
			aboutItem.addActionListener( aboutListener );

			//Adding some menu separator
			popupMenu.addSeparator();

			//Quit menu to terminate the tray icon by disposing the tray icon
			final JMenuItem exitItem = new JMenuItem( "Quit" ); //$NON-NLS-1$
			popupMenu.add( exitItem );
			exitItem.addActionListener( exitListener );


			//Hidden dialog displayed behing the system tray to auto hide the popup menu when clicking somewhere else on the screen
			final JDialog hiddenDialog = new JDialog ();
			hiddenDialog.setSize( 10, 10 );

			//Listener based on the focus to auto hide the hidden dialog and the popup menu when the hidden dialog box lost focus
			hiddenDialog.addWindowFocusListener(new WindowFocusListener () {

				public void windowLostFocus ( final WindowEvent e ) {
					hiddenDialog.setVisible( false );
				}

				public void windowGainedFocus ( final WindowEvent e ) {
					//Nothing to do
				}
			});


			//Add a listener to display the popupmenu and the hidden dialog box when the tray icon is clicked
			trayIcon.addMouseListener( new MouseAdapter() {

				public void mouseReleased( final MouseEvent e) {

					if( e.isPopupTrigger() ) {
						//Display the menu at the position of the mouse
						//The dialog is also displayed at this position but it is behind the system tray
						popupMenu.setLocation( e.getX(), e.getY() );
						hiddenDialog.setLocation( e.getX(), e.getY() );

						//Important: set the hidden dialog as the invoker to hide the menu with this dialog lost focus
						popupMenu.setInvoker( hiddenDialog );
						hiddenDialog.setVisible( true );
						popupMenu.setVisible( true );
					}
				}
			});

			//Add the icon to the system tray
			try {
				tray.add( trayIcon );
			}
			catch ( final AWTException e ) {

				final StringWriter stringWriter = new StringWriter();
				e.printStackTrace( new PrintWriter( stringWriter ) );
				JOptionPane.showMessageDialog( null, "tray icon cannot be added to the system tray" + System.lineSeparator() + e.getMessage() + System.lineSeparator() + stringWriter.toString(), "Tray icon skeleton initialization error", JOptionPane.ERROR_MESSAGE ); //$NON-NLS-1$

				System.exit( 2 );
			}
		}
		else {
			//if the System is not compatible with SystemTray
			JOptionPane.showMessageDialog( null, "SystemTray cannot be initialized" + System.lineSeparator() + "this system is not compatible!", "Tray icon skeleton initialization error", JOptionPane.ERROR_MESSAGE ); //$NON-NLS-1$ //$NON-NLS-2$

			System.exit( 1 );
		}
	}
}
