package org.dschweie.neoload.advancedactions.qf_test.command.library;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.neotys.extensions.action.ActionParameter;

/**
 *  \brief  Diese Klasse enthält Methoden zur Generierung von Anweisungen über die Kommandozeile
 *
 *  Aus den einzelnen Aktionsparametern, die der Anwender über die NeoLoad-GUI
 *  erfasst hat, müssen Parameter für den Kommandozeilenaufruf gebildet werden.
 *
 *  Diese Klasse stellt geeignete Klassenmethoden bereit, die aus der Liste
 *  der ActionParameter die entsprechenden Optionen mit ihren Werten auswerten.
 *
 *  Zusätzlich bietet die Klasse noch Hilfsfunktionen an, die von der Klasse
 *  ActionParameter nicht bereitgestellt werden.
 *
 *  @author dirk.schweier
 *  @since  0.1.0
 */
final public class QFTestCommandElementsLibrary
{
  /**
   *  \brief  Die Methode erzeugt Optionen zur Angabe des Daemon
   *
   *  Diese Methode liefert die Optionen \c -daemonhost und \c -daemonport
   *  zurück. Diese Information wird im Aufruf benötigt, damit der Dienst
   *  unter dem richtigen Rechnername bzw. IP und dem richtigen Port
   *  angesprochen bzw. gestartet wird.
   *
   *  Sollten keine Informationen in den \b parameters zu finden sein, wird
   *  \c localhost:3543 als Default-Einstellung gestetzt.
   *
   *  @param  parameters      In dem Parameter wird eine Liste der
   *                          ActionParameter übergeben, die der Anwender
   *                          über die GUI von NeoLoad erfasst hat.
   *
   *  @return Die Methode liefert im Rückgabewert eine Liste von Strings,
   *          die den zu setzenden Optionen entsprechen.
   */
  final public static List<String> getDaemonDestinationElements(List<ActionParameter> parameters)
  {
    final List<String>  elements  = new Vector<String>();
    elements.add("-daemonport");
    elements.add(QFTestCommandElementsLibrary.getParameterValue(parameters, "daemonport", "3543"));
    elements.add("-daemonhost");
    elements.add(QFTestCommandElementsLibrary.getParameterValue(parameters, "daemonhost", "localhost"));
    return elements;
  }

  public static final List<String> getJavaVMParameters(List<ActionParameter> parameters)
  {
    List<String>  retval            = new Vector<String>();
    Iterator<ActionParameter> it    = parameters.iterator();

    while(it.hasNext())
    {
      ActionParameter current = it.next();
      if("jvm".equals(current.getName().toLowerCase().trim()))
      { //  ActionParameter ist ein Java VM Parameter
        String[] elements = current.getValue().trim().split("\\s+");
        for(int i = 0; i < elements.length; ++i)
        {
          if(0==i)
            retval.add(elements[i].startsWith("-J")?elements[i]:"-J".concat(elements[i]));
          else
            retval.add(elements[i]);
        }
      }
    }

    return retval;
  }

  /**
   *  \brief  Die Methode erzeugt eine optionale Option Steuerung des Rückgeabewertes von QF-Test
   *
   *  QF-Test signalisiert über den Rückgabewert, ob während der Ausführung
   *  Warnungen, Fehler oder Exceptions aufgetreten sind. Nur wenn alles
   *  fehlerfrei ausgeführt wurde, wird der Wert 0 zurückgegeben.
   *
   *  In einem Lasttest kann es sein, dass Warnungen und Fehler im funktionalen
   *  Bereich nicht von Relevanz sind. In diesem Fall kann der Anwender in
   *  NeoLoad mit dem optionalen Parameter \c exitcodeignore bewirken, dass
   *  Ereignisse unterhalb einer bestimmten Schwelle nicht über den
   *  Rückgabewert signalisiert werden und somit der Rückgabewert 0 ist.
   *
   *  @param  parameters      In dem Parameter wird eine Liste der
   *                          ActionParameter übergeben, die der Anwender
   *                          über die GUI von NeoLoad erfasst hat.
   *  @return Die Methode liefert im Rückgabewert eine Liste von Strings,
   *          die den zu setzenden Optionen entsprechen.
   */
  public static final List<String> getExitcodeSettings(List<ActionParameter> parameters)
  {
    List<String>  retval            = new Vector<String>();
    String        strExitCodeValue  = getParameterValue(parameters, "exitcodeignore", "no value").toLowerCase().trim();

    if(!("no value".equals(strExitCodeValue)))
    { //  In diesem Fall wurde ein Wert übergeben. Der Parameter wird nur bei gültigem Wert gesetzt.
      if("exception".equals(strExitCodeValue))
        retval.add("-exitcodeignoreexception");
      else if("error".equals(strExitCodeValue))
        retval.add("-exitcodeignoreerror");
      else if("warning".equals(strExitCodeValue))
        retval.add("-exitcodeignorewarning");
    }

    return retval;
  }

  public static final List<String> getSecuritySettings(List<ActionParameter> parameters)
  {
    List<String>  retval            = new Vector<String>();
    String        strKeystoreValue  = getParameterValue(parameters, "keystore", "no value").toLowerCase().trim();
    String        strKeypassValue   = getParameterValue(parameters, "keypass", "no value").toLowerCase().trim();

    if(!("no value".equals(strKeystoreValue)))
    { //  In diesem Fall wurde ein Wert übergeben. Der Parameter wird nur bei gültigem Wert gesetzt.
      if("".equals(strKeystoreValue))
      { //  In diesem Fall gilt ein leerer Keystore.
        retval.add("-keystore=");
      }
      else
      { //  Es wurde ein Keystore parametriert und damit wird Keystore und Passwort gesetzt.
        retval.add("-keystore");
        retval.add(strKeystoreValue);
        retval.add("-keypass");
        retval.add(strKeypassValue);
      }
    }

    return retval;
  }

  /**
   *  \brief  Die Methode liefert die Option zum Logging durch QF-Test
   *
   *  QF-Test kann die Ausführung von QF-Test-Skripten protokollieren und
   *  erzeugt eine Log-Datei.
   *
   *  Wenn in einem Lasttest die Erzeugung der Log-Dateien erw�nscht ist, dann
   *  kann der Anwender in NeoLoad über den Parameter \c runlog den Namen für
   *  die zu erstellende Protokolldatei angeben.
   *
   *  Wenn der optionale Parameter nicht in der Liste der ActionParameter
   *  enthalten ist, wird eine Option gesetzt, die das Erzeugen der Log-Datei
   *  unterdr�ckt.
   *
   *  @param  parameters      In dem Parameter wird eine Liste der
   *                          ActionParameter übergeben, die der Anwender
   *                          über die GUI von NeoLoad erfasst hat.
   *  @return Die Methode liefert im Rückgabewert eine Liste von Strings,
   *          die den zu setzenden Optionen entsprechen.
   */
  public static final List<String> getLogSettings(List<ActionParameter> parameters)
  {
    List<String>  retval      = new Vector<String>();
    String        strLogValue = getParameterValue(parameters, "runlog", "no value").trim();

    if(!("no value".equals(strLogValue)))
    { //  In diesem Fall wurde ein Wert übergeben. Der Parameter wird nur bei gültigem Wert gesetzt.
      retval.add("-runlog");
      retval.add(strLogValue);
    }
    else
    { //  andernfalls sorgt dieser Schl�ssel für das Unterdr�cken des Logfiles.
      retval.add("-nolog");
    }

    return retval;
  }

  public static final List<String> getOptionsSettings(List<ActionParameter> parameters)
  {
    List<String>  retval      = new Vector<String>();
    String        strOptions = getParameterValue(parameters, "options", "none").trim();

    if(!("none".equals(strOptions)))
    { //  In diesem Fall wurde ein Wert übergeben. Der Parameter wird nur bei gültigem Wert gesetzt.
      retval.add("-options");
      retval.add(strOptions);
    }

    return retval;
  }

  public static final List<String> getConfigSettings(List<ActionParameter> parameters)
  {
    List<String>  retval        = new Vector<String>();
    String        strSystemCfg  = getParameterValue(parameters, "systemcfg", "none").trim();
    String        strUserCfg    = getParameterValue(parameters, "usercfg", "none").trim();

    if(!("none".equals(strSystemCfg)))
    { //  In diesem Fall wurde ein Wert übergeben. Der Parameter wird nur bei gültigem Wert gesetzt.
      retval.add("-systemcfg");
      retval.add(strSystemCfg);
    }

    if(!("none".equals(strUserCfg)))
    { //  In diesem Fall wurde ein Wert übergeben. Der Parameter wird nur bei gültigem Wert gesetzt.
      retval.add("-usercfg");
      retval.add(strUserCfg);
    }

    return retval;
  }

  public static final List<String> getCleanSettings(List<ActionParameter> parameters)
  {
    List<String>  retval        = new Vector<String>();
    String        strStartClean = getParameterValue(parameters, "startclean", "false").trim();
    String        strStopClean  = getParameterValue(parameters, "stopclean", "false").trim();

    if(!("false".equals(strStartClean)))
    { //  In diesem Fall wurde ein Wert übergeben. Der Parameter wird nur bei gültigem Wert gesetzt.
      retval.add("-startclean");
    }

    if(!("false".equals(strStopClean)))
    { //  In diesem Fall wurde ein Wert übergeben. Der Parameter wird nur bei gültigem Wert gesetzt.
      retval.add("-stopclean");
    }

    return retval;
  }

  /**
   *  \brief  Die Methode liefert zu einem Parameter den entsprechenden Wert
   *
   *  In NeoLoad kann der Anwender zu einer Advanced Action Paare aus Parameter
   *  und Wert erfassen. Diese Paare werden als Instanzen der Klasse
   *  ActionParameter in einer Liste verwaltet. Diese Liste wird der
   *  entsprechenden Implementierung der Schnittstelle ActionEngine übergeben.
   *
   *  Mit dieser Methode kann die Liste auf einen bestimmten Parameter
   *  durchsucht werden. Wenn dieser gefunden wird, wird der gespeicherte
   *  Wert zurückgegeben. Andernfalls wird der Wert \b substitude
   *  zurückgegeben.
   *
   *  @param  parameters      In dieser Variablen wird die Liste der
   *                          ActionParameter �begeben, in der nach dem
   *                          Parameter \b key gesucht werden soll.
   *  @param  key             In dieser Variablen ist der Name des Parameters
   *                          zu übergeben, nach dem in der List gesucht werden
   *                          soll, die in \b parameters übergeben wird.
   *  @param  substitude      In dieser Variablen soll der Aufrufer einen Wert
   *                          übergeben, der als Rückgabewert gew�hlt werden
   *                          soll, wenn der Parameter in der Liste nicht
   *                          gefunden wurde.
   *
   *  @return Die Methode liefert als Rückgabewert einen String, der dem Wert
   *          entspicht, der im Parameter \b key gefunden wurde und den Wert
   *          \b substitude, wenn \b key nicht in \b parameters gefunden wurde.
   */
  public static final String getParameterValue(List<ActionParameter> parameters, String key, String substitude)
  {
    String value = substitude;

    for(int i = 0; i < parameters.size(); ++i)
    {
      if(parameters.get(i).getName().equals(key))
      {
        value = parameters.get(i).getValue();
      }
    }

    return value;
  }

  /**
   *  \brief  Die Methode liefert den Aufruf von QF-Test über die Kommandozeile zurück
   *
   *  Diese Methode erzeugt den Teil des Aufrufs, der \c qftest.exe und der
   *  Option \c -batch besteht.
   *
   *  Sofern in \b parameters ein Pfad zum Programmverzeichnis übergeben wird,
   *  wird dieses berücksichtigt und vorangestellt.
   *
   *  @param  parameters      In dem Parameter wird eine Liste der
   *                          ActionParameter übergeben, die der Anwender
   *                          über die GUI von NeoLoad erfasst hat.
   *
   *  @return Die Methode liefert im Rückgabewert eine Liste von Strings,
   *          die den zu setzenden Optionen entsprechen.
   *
   *  \see    org.dschweie.neoload.advancedactions.qf_test.command.library.QFTestCommandElementsLibrary.getQFTestBatchCall(List<ActionParameter>, String)
   */
  public static final List<String> getQFTestBatchCall(List<ActionParameter> parameters)
  {
    return getQFTestBatchCall(parameters, "qftest.exe");
  }

  /**
   *  \brief  Die Methode liefert den Aufruf von QF-Test über die Kommandozeile zurück
   *
   *  Diese Methode erzeugt den Teil des Aufrufs einer speziellen .exe-Datei
   *  über den Parameter \b executable und der Option \c -batch besteht.
   *
   *  Sofern in \b parameters ein Pfad zum Programmverzeichnis übergeben wird,
   *  wird dieses berücksichtigt und vorangestellt.
   *
   *  @param  parameters      In dem Parameter wird eine Liste der
   *                          ActionParameter übergeben, die der Anwender
   *                          über die GUI von NeoLoad erfasst hat.
   *  @param  executable      In dem Parameter wird der Dateiname erwartet,
   *                          der in den Aufruf eingebaut wird.
   *
   *  @return Die Methode liefert im Rückgabewert eine Liste von Strings,
   *          die den zu setzenden Optionen entsprechen.
   */
  public static final List<String> getQFTestBatchCall(List<ActionParameter> parameters, String executable)
  {
    final List<String>  elements  = new Vector<String>();
    if(hasParameterKey(parameters, "qftestPath"))
      elements.add(getParameterValue(parameters,"qftestPath","").concat("qftest.exe"));
    else
      elements.add(executable);
    elements.add("-batch");
    return elements;
  }

  /**
   *  \brief  Die Methode liefert den Aufruf von QF-Test über die Kommandozeile zurück
   *
   *  Diese Methode erzeugt den Teil des Aufrufs, der \c qftestc.exe und der
   *  Option \c -batch besteht.
   *
   *  Sofern in \b parameters ein Pfad zum Programmverzeichnis übergeben wird,
   *  wird dieses berücksichtigt und vorangestellt.
   *
   *  @param  parameters      In dem Parameter wird eine Liste der
   *                          ActionParameter übergeben, die der Anwender
   *                          über die GUI von NeoLoad erfasst hat.
   *  @return Die Methode liefert im Rückgabewert eine Liste von Strings,
   *          die den zu setzenden Optionen entsprechen.
   *
   *  \see    org.dschweie.neoload.advancedactions.qf_test.command.library.QFTestCommandElementsLibrary.getQFTestBatchCall(List<ActionParameter>, String)
   */
  public static final List<String> getQFTestCBatchCall(List<ActionParameter> parameters)
  {
    return getQFTestBatchCall(parameters, "qftestc.exe");
  }

  /**
   *  \brief  Die Methode liefert Optionen des Testknotens, der ausgeführt werden soll
   *
   *  Am Ende des Aufrufs von QF-Test wird die Nennung des Knotens erwartet,
   *  der von QF-Test ausgeführt werden soll. Hier ist eine .qft-Datei und der
   *  Name des Knotens zu nennen, der über die Liste \b parameters zu übergeben
   *  ist.
   *
   *  Optional kann der Anwender mit dem Parameter \c suitedir ein Verzeichnis
   *  benennen, in dem sich die notwendigen .qft-Dateien befinden.
   *
   *  @param  parameters      In dem Parameter wird eine Liste der
   *                          ActionParameter übergeben, die der Anwender
   *                          über die GUI von NeoLoad erfasst hat.
   *
   *  @return Die Methode liefert im Rückgabewert eine Liste von Strings,
   *          die den zu setzenden Optionen entsprechen.
   */
  public static final List<String> getTestCaseSettings(List<ActionParameter> parameters)
  {
    List<String>  retval            = new Vector<String>();
    String        strSuiteDirValue  = getParameterValue(parameters, "suitedir", "no value").trim();

    //  Zun�chst wird der optionale Parameter "-suitedir" hinzugef�gt.
    if(!("no value".equals(strSuiteDirValue)))
    { //  In diesem Fall wurde ein Wert übergeben. Der Parameter wird nur bei gültigem Wert gesetzt.
      retval.add("-suitedir");
      retval.add(strSuiteDirValue);
    }

    //  Der letzte Parameter ist die Angabe des Testfall, der ausgeführt werden soll.
    retval.add(getParameterValue(parameters, "testcase", "").trim());

    return retval;
  }

  public static final List<String> getLicenseSettings(List<ActionParameter> parameters)
  {
    List<String>  retval                  = new Vector<>();
    String        strLicenseFileValue     = getParameterValue(parameters, "license", "no value").trim();
    String        strLicenseWaitForValue  = getParameterValue(parameters, "license.waitfor", "no value").trim();

    if(!("no value".equals(strLicenseFileValue)))
    { //  In diesem Fall wurde ein Wert übergeben. Der Parameter wird nur bei gültigem Wert gesetzt.
      retval.add("-license");
      retval.add(strLicenseFileValue);
    }

    if(!("no value".equals(strLicenseWaitForValue)))
    { //  In diesem Fall wurde ein Wert übergeben. Der Parameter wird nur bei gültigem Wert gesetzt.
      retval.add("-license.waitfor");
      retval.add(strLicenseWaitForValue);
    }

    return retval;
  }

  public static final List<String> getPluginDirSettings(List<ActionParameter> parameters)
  {
    List<String>  retval            = new Vector<>();
    String        strPluginDirValue  = getParameterValue(parameters, "plugindir", "no value").trim();

    if(!("no value".equals(strPluginDirValue)))
    { //  In diesem Fall wurde ein Wert übergeben. Der Parameter wird nur bei gültigem Wert gesetzt.
      retval.add("-plugindir");
      retval.add(strPluginDirValue);
    }

    return retval;
  }

  /**
   *  \brief  Die Methode liefert den Anteil der Variablenübergabe an QF-Test
   *
   *  Um die Testskripte variabel einsetzen zu k�nnen, k�nnen Variablen über
   *  die Kommandozeile mit Werten versorgt werden.
   *
   *  Der Anwender kann über entsprechende ActionParameter Werte in die Liste
   *  \b parameters einstellen, die durch diese Methode ausgelesen und in das
   *  Format zur übergabe in die Kommandozeile übersetzt werden.
   *
   *  \note   Es wird davon ausgegangen, dass die Reihenfolge der Variablen
   *          für den Ablauf des Testskripts ohne Belang ist. Die Variablen
   *          werden mit den entsprechenden Werten in der Reihenfolge
   *          erstellt, in der sie in der Liste abgelegt sind. <br/>
   *          Es ist nicht sichergestellt, dass die Reihenfolge in der GUI
   *          gleich ist mit der Reihenfolge in der Liste \b parameters
   *
   *  @param  parameters      In dem Parameter wird eine Liste der
   *                          ActionParameter übergeben, die der Anwender
   *                          über die GUI von NeoLoad erfasst hat.
   *
   *  @return Die Methode liefert im Rückgabewert eine Liste von Strings,
   *          die den zu setzenden Optionen entsprechen.
   */
  public static final List<String> getVariableSettings(List<ActionParameter> parameters)
  {
    List<String> retval = new Vector<String>();
    Iterator<ActionParameter> it = parameters.iterator();

    while(it.hasNext())
    {
      ActionParameter current = it.next();
      if("variable".equals(current.getName()))
      { //  ActionParameter ist Variable und somit muss sie in Kommando eingesetzt werden.
        retval.add("-variable");
        retval.add(current.getValue());
      }
    }
    return retval;
  }

  /**
   *  \brief  Die Methode liefert optionale Kommandozeilenparameter zur Steuerung der Ausgabe
   *
   *  W�hrend der Ausführung eines Tests kann QF-Test bestimmte Informationen
   *  zum Testablauf über die Kommandozeile protokollieren.
   *
   *  Der Anwender kann optional in der Oberfläche von NeoLoad das Verhalten
   *  steuern. Die Ausgaben in der Kommandozeile werden in der Response der
   *  Advanced Action protokolliert und k�nnen so dem Anwender n�tzlich sein,
   *  um in NeoLoad Validierungen zu erstellen oder Inhalte in Variablen zu
   *  übernehmen.
   *
   *  @param  parameters      In dem Parameter wird eine Liste der
   *                          ActionParameter übergeben, die der Anwender
   *                          über die GUI von NeoLoad erfasst hat.
   *  @return Die Methode liefert im Rückgabewert eine Liste von Strings,
   *          die den zu setzenden Optionen entsprechen.
   */
  public static final List<String> getVerboseSettings(List<ActionParameter> parameters)
  {
    List<String>  retval          = new Vector<String>();
    String        strVerboseLevel = getParameterValue(parameters, "verbose", "no value").toLowerCase().trim();

    if(!("no value".equals(strVerboseLevel)))
    { //  In diesem Fall sind Einstellungen erforderlich.
      retval.add("-verbose");
      retval.add(strVerboseLevel);
    }

    return retval;
  }

  /**
   *  \brief  Die Methode durchsucht eine Liste nach einem Parameter mit dem Namen \b key
   *
   *  Mit dieser Methode kann die Liste \b parameters auf einen Parameter
   *  \b key durchsucht werden. Die Methode signalisiert über den Rückgabewert,
   *  ob der Parameter gefunden wurde oder nicht.
   *
   *  @param  parameters      In dem Parameter wird eine Liste der
   *                          ActionParameter übergeben, die der Anwender
   *                          über die GUI von NeoLoad erfasst hat.
   *  @param  key             In dem Parameter wird ein String erwartet,
   *                          der für das Schlüsselwort des Parameters
   *                          steht, der in der Liste \b parameters
   *                          gesucht werden soll.
   *
   *  @return Die Methode liefert den Wert \c true, wenn in der Liste
   *          \b parameters der Parameter \b key gefunden wurde.
   *          Andernfalls wird \c false zurückgegeben.
   */
  public static final boolean hasParameterKey(List<ActionParameter> parameters, String key)
  {
    boolean flag = false;

    for(int i = 0; i < parameters.size(); ++i)
    {
      flag |= parameters.get(i).getName().equals(key);
    }

    return flag;
  }
}
