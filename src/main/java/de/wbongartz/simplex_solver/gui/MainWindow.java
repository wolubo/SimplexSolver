package gui;

import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.html.HTMLEditorKit;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import parser.ParseException;
import parser.TokenMgrError;
import process.*;
import solver.*;

/**
 * Die Klasse MainWindow implementiert das Hauptfenster. Das Hauptfenster besteht aus zwei Bereichen:
 * - Das Feld für die Eingabe des Optimierungsproblems (im linken Fensterbereich) mit den drei Buttons 
 *   für die Ablaufsteuerung ('Lösen', 'Nächster Schritt' und 'Neustart') und 
 * - das Ausgabe-Feld (im rechten Fensterbereich), in dem die Ausgabe der einzelnen Lösungsschritte 
 *   angezeigt wird.
 * Die Ausgabe erfolgt dabei im HTML-Format. Die Formatierung wird durch die Festlegungen im File
 * /ressources/SimplexSolver.css festgelegt.
 * 
 * @author Wolfgang Bongartz
 *
 */
public class MainWindow extends JFrame implements DocumentListener, StateChangedListener, StepExecutedListener {

	private static final long serialVersionUID = 1L;

	private JTextArea _editorPane;
	private JTextPane _showPane;
	private HTMLEditorKit _eKit;
	private JSplitPane _splitPane;
	private JButton _runButton, _stepButton, _btnRestart;

	private String _filename;
	private Preferences _preferences;
	private ProcessManager _processManager;
	
	private String _showPaneContent;

	private boolean _hasUnsavedChanges;


	/**
	 * Erzeugt das Hauptfenster. Lädt die Präferenzen des Users sowie das CSS-File und öffnet das zuletzt benutzte Optimierungsproblem.
	 */
	public MainWindow() {
		super("SimplexSolver");

		Preferences root = Preferences.userRoot();
		_preferences = root.node("/com/wlb/SimplexSolver");

		int left = _preferences.getInt("left", 0);
		int top = _preferences.getInt("top", 0);
		int width = _preferences.getInt("width", 900);
		int height = _preferences.getInt("height", 600);
		setBounds(left, top, width, height);

		_filename = _preferences.get("filename", "." + File.separator + "examples" + File.separator + "fahrradbeispiel.lpp");
		_processManager = new ProcessManager();
		_processManager.addStateChangedListener(this);
		_processManager.addStepExecutedListener(this);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Font font = new Font("Courier", Font.PLAIN, 14);
		
		getContentPane().setLayout(new BorderLayout());

		_splitPane = new JSplitPane();
		_splitPane.setOneTouchExpandable(true);
		int dividerLocation = _preferences.getInt("DividerLocation", 400);
		_splitPane.setDividerLocation(dividerLocation);
		getContentPane().add(_splitPane);

		JPanel rightPane = new JPanel(new BorderLayout());
		_splitPane.setRightComponent(rightPane);

		_showPaneContent = new String();
		
		_showPane = new JTextPane();
		_showPane.setEditable(false);
		_eKit = new HTMLEditorKit();
		_eKit.getStyleSheet().importStyleSheet(this.getClass().getResource("/ressources/SimplexSolver.css"));
		_showPane.setEditorKit(_eKit);
		_showPane.setFont(font);
		JScrollPane showScrollPane = new JScrollPane(_showPane);
		rightPane.add(showScrollPane, BorderLayout.CENTER);

		// Text setzen
		_showPane.setText("<HTML><BODY><b>ein Html-Text</b><br> zum Testen </BODY></HTML>");
		

		JPanel leftPane = new JPanel(new BorderLayout());
		_splitPane.setLeftComponent(leftPane);

		_editorPane = new JTextArea();
		_editorPane.setLineWrap(false);
		_editorPane.setFont(font);
		_editorPane.getDocument().addDocumentListener(this);
		JScrollPane editorScrollPane = new JScrollPane(_editorPane);
		leftPane.add(editorScrollPane, BorderLayout.CENTER);

		JPanel buttonPane = new JPanel();

		_runButton = new JButton("Lösen");
		_runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performNextStep(true);
			}

		});
		buttonPane.add(_runButton);

		_stepButton = new JButton("Nächster Schritt");
		_stepButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performNextStep(false);
			}
		});
		buttonPane.add(_stepButton);

		_btnRestart = new JButton("Neustart");
		_btnRestart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_processManager.initialize(_editorPane.getText());
				initShowPane();
			}
		});
		buttonPane.add(_btnRestart);

		leftPane.add(buttonPane, BorderLayout.SOUTH);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("Datei");
		menuBar.add(mnFile);

		JMenuItem mntmNew = new JMenuItem("Neu");
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				newFile();
			}
		});
		mnFile.add(mntmNew);

		JMenuItem mntmLoad = new JMenuItem("Laden...");
		mntmLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadFile(null);
			}
		});
		mnFile.add(mntmLoad);

		JMenuItem mntmSave = new JMenuItem("Speichern");
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveFile(_filename);
			}
		});
		mnFile.add(mntmSave);

		JMenuItem mntmSaveAs = new JMenuItem("Speichern als...");
		mntmSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveFile(null);
			}
		});
		mnFile.add(mntmSaveAs);

		JMenuItem mntmQuit = new JMenuItem("Beenden");
		mntmQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(saveChanges()) {
					_preferences.putInt("left", getX());
					_preferences.putInt("top", getY());
					_preferences.putInt("width", getWidth());
					_preferences.putInt("height", getHeight());
					_preferences.putInt("DividerLocation", _splitPane.getDividerLocation());
					dispose();
				}
			}
		});
		mnFile.add(mntmQuit);

		JMenu mnHelp = new JMenu("Hilfe");
		menuBar.add(mnHelp);

		JMenuItem mntmHelp = new JMenuItem("Hilfe");
		mntmHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showHelp();
			}
		});
		mnHelp.add(mntmHelp);

		JMenuItem mntmAbout = new JMenuItem("Über SimplexSolver");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showAbout();
			}
		});
		mnHelp.add(mntmAbout);

		loadFile(_filename);
	}

	private void showHelp() {
		String msg = "";
		msg += "Das Programm löst Optimierungsprobleme durch die Anwendung des Simplex-Verfahrens. ";
		msg += "Es dient dazu, das Erlernen dieses Verfahrens zu erleichtern.\n";
		msg += "Linker Bereich: Editor für lineare Optimierungsprobleme.\n";
		msg += "Rechter Bereich: Anzeige des Lösungswegs.\n";
		msg += "Button 'Lösen': Zeige die komplette Lösung an\n";
		msg += "Button 'Nächster Schritt': Zeige den nächsten Lösungsschritt an\n";
		msg += "Button 'Neustart': Beginne den Lösungsweg von neuem";
		JOptionPane.showMessageDialog(null,msg, "Hilfe",JOptionPane.INFORMATION_MESSAGE);
	}

	private void showAbout() {
		String msg = "";
		msg += "SimplexSolver ist eine Projektarbeit von Wolfgang Bongartz, die im Rahmen\n";
		msg += "des VAWi-Studiums an der Universität Duisburg/Essen erstellt wurde.\n";
		msg += "Betreuung: Prof. Dr. Natalia Kliewer\n";
		msg += "© Wolfgang Bongartz, 2016\n";
		msg += "✍ mail@wolfgang-bongartz.de";
		JOptionPane.showMessageDialog(null,msg, "Über SimplexSolver",JOptionPane.INFORMATION_MESSAGE);
	}

	private void saveFile(String filename) {
		if(filename==null) {
			FileDialog fd = new FileDialog(this, "Sichern", FileDialog.SAVE);
			fd.setDirectory("." + File.separator + "examples");
			fd.setFile(_filename);
			fd.setVisible(true);
			filename = fd.getFile();
			String directory = fd.getDirectory();
			if(directory!=null && directory.length()>0) {
				filename = directory + File.separator + filename;
			}
		}
		if (filename != null) {
			_filename = filename;
			_preferences.put("filename", _filename);
			String content = _editorPane.getText();
			BufferedWriter writer = null;
			try {
				File logFile = new File(filename);
				writer = new BufferedWriter(new FileWriter(logFile));
				writer.write(content);
				_hasUnsavedChanges=false;
				this.setTitle("SimplexSolver: " + _filename);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null,e.getMessage(),"Datei kann nicht geschrieben werden", JOptionPane.ERROR_MESSAGE);
			} finally {
				try {
					// Close the writer no matter what...
					writer.close();
				} catch (Exception e) {
				}
			}
		}
	}

	private void newFile() {
		if(!saveChanges()) return;
		
		_filename = "." + File.separator + "examples" + File.separator + "neu.lpp";
		_editorPane.setText("");
		_processManager.initialize("");
		initShowPane();
		_hasUnsavedChanges=false;
		this.setTitle("SimplexSolver: " + _filename);
	}

	private void loadFile(String filename) {
		if(!saveChanges()) return;

		if(filename==null) {
			FileDialog fd = new FileDialog(this, "Laden",
					FileDialog.LOAD);
			fd.setDirectory("." + File.separator + "examples");
			fd.setFile("*.lpp");
			fd.setVisible(true);
			filename = fd.getFile();
			String directory = fd.getDirectory();
			if(directory!=null && directory.length()>0) {
				filename = directory + File.separator + filename;
			}
		}
		if (filename != null) {
			_filename = filename;
			_preferences.put("filename", _filename);
			BufferedReader br = null;
			String content = "";
			try {
				br = new BufferedReader(new FileReader(filename));
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();
				while (line != null) {
					sb.append(line + "\n");
					line = br.readLine();
				}
				content = sb.toString();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Datei kann nicht geladen werden: " + e.getMessage() + "!", "Datei kann nicht geladen werden", JOptionPane.ERROR_MESSAGE);
			} finally {
				if (br != null) {
					try {
						// Close the reader no matter what...
						br.close();
					} catch (Exception e) {
					}
				}
			}
			_editorPane.setText(content);
			_processManager.initialize(content);
			initShowPane();
			_hasUnsavedChanges=false;
			this.setTitle("SimplexSolver: " + _filename);
		}
	}

	private boolean saveChanges() {
		if(_hasUnsavedChanges) {

			Object[] options = {"Ja", "Nein", "Abbrechen"};
			int n = JOptionPane.showOptionDialog(this, "Sollen Änderungen gespeichert werden?",
					"Ungesicherte Änderungen",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					options,
					options[0]);
			if(n==2) return false;
			if(n==0) saveFile(_filename);
		}
		return true;
	}

	private void performNextStep(boolean runTrough) {
		try {
			if(runTrough)
				_processManager.run();
			else
				_processManager.step();
		} catch(TokenMgrError ex) {
			JOptionPane.showMessageDialog(null, "Lexikalischer Fehler: " + ex.getMessage() + "!", "Lexikalischer Fehler in der Definition", JOptionPane.ERROR_MESSAGE);
		} catch(ProcessError ex) {
			String headline = "Prozess-Fehler";
			Exception innerException = ex.getOriginalException();
			if(innerException!=null) {
				if(innerException instanceof ParseException) {
					headline = "Fehler in der Definition";
				}
			} else {
				innerException = ex;
			}
			JOptionPane.showMessageDialog(null,innerException.getMessage(), headline, JOptionPane.ERROR_MESSAGE);
		} catch(Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null,ex.getMessage(),"Interner Fehler", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Event-Handler: Der Benutzer hat eine Änderung im Eingabebereich vorgenommen. Das Programm wird neu initialisiert.
	 */
	@Override
	public void insertUpdate(DocumentEvent e) {
		docHasChanged();
	}

	/**
	 * Event-Handler: Der Benutzer hat eine Änderung im Eingabebereich vorgenommen. Das Programm wird neu initialisiert.
	 */
	@Override
	public void removeUpdate(DocumentEvent e) {
		docHasChanged();
	}

	/**
	 * Event-Handler: Der Benutzer hat eine Änderung im Eingabebereich vorgenommen. Das Programm wird neu initialisiert.
	 */
	@Override
	public void changedUpdate(DocumentEvent e) {
		docHasChanged();
	}

	private void docHasChanged() {
		this.setTitle("SimplexSolver: " + _filename + " *");
		_hasUnsavedChanges =true;
		_processManager.initialize(_editorPane.getText());
		initShowPane();
	}

	/**
	 * Behandelt die vom ProcessMaster versendeten StateChanged-Events.
	 * Ein-/Ausblenden der Buttons abhängig vom aktuellen Bearbeitungsstatus.
	 */
	@Override
	public void processStateChanged(ProcessState prevState,	ProcessState newState) {
		switch(newState) {
		case ENDED:
			_runButton.setEnabled(false);
			_stepButton.setEnabled(false);
			_btnRestart.setEnabled(true);
			break;
		case ERROR:
			_runButton.setEnabled(false);
			_stepButton.setEnabled(false);
			_btnRestart.setEnabled(true);
			break;
		case INITIALIZED:
			_runButton.setEnabled(true);
			_stepButton.setEnabled(true);
			_btnRestart.setEnabled(false);
			break;
		case NOT_INITIALIZED:
			_runButton.setEnabled(false);
			_stepButton.setEnabled(false);
			_btnRestart.setEnabled(true);
			break;
		case RUNNING:
			_runButton.setEnabled(true);
			_stepButton.setEnabled(true);
			_btnRestart.setEnabled(true);
			break;
		default:
			_runButton.setEnabled(false);
			_stepButton.setEnabled(false);
			_btnRestart.setEnabled(true);
			break;
		}
	}

	/**
	 * Behandelt die vom ProcessMaster versendeten StepExecuted-Events.
	 * Anhängen des Zwischenergebnisses an den Anzeigebereich.
	 */
	@Override
	public void processStepExecuted(ProcessState currentState, Object result) {

		if(result!=null) {
			@SuppressWarnings("unchecked")
			ArrayList<String> output = (ArrayList<String>) result;
			showResult(output);
		}
	}

	private void showResult(ArrayList<String> result) {
		for(String s: result) {
			_showPaneContent += s + "\n";
		}

		if(result.size()>0) {
			_showPane.setText(_showPaneContent);
//			try {
//				_showDocument.insertString(_showDocument.getLength(), data, null);
//				_eKit.insertHTML(_showDocument, _showDocument.getLength(), data, 0, 0, null);
//			} catch (BadLocationException | IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			_showPane.setText("\n" + data);
		}		
	}
	
	private void initShowPane() {
		_showPane.setText("");
		_showPaneContent = new String();
//		_showPaneContent += "<!DOCTYPE html>";
//		_showPaneContent += "<meta charset=\"UTF-8\"/>";
	}
}
