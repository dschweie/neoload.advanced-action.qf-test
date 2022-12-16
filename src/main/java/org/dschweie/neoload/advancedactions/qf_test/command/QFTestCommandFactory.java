package org.dschweie.neoload.advancedactions.qf_test.command;

import java.util.List;

import org.dschweie.neoload.advancedactions.qf_test.command.worker.QFTestCommandWorkerPing;
import org.dschweie.neoload.advancedactions.qf_test.command.worker.QFTestCommandWorkerStartDaemon;
import org.dschweie.neoload.advancedactions.qf_test.command.worker.QFTestCommandWorkerStopDaemon;
import org.dschweie.neoload.advancedactions.qf_test.command.worker.QFTestCommandWorkerTestCallDaemon;
import org.dschweie.neoload.advancedactions.qf_test.daemon.QFTestExecuteAction;
import org.dschweie.neoload.advancedactions.qf_test.daemon.QFTestPingAction;
import org.dschweie.neoload.advancedactions.qf_test.daemon.QFTestStartAction;
import org.dschweie.neoload.advancedactions.qf_test.daemon.QFTestStopAction;
import com.neotys.extensions.action.ActionParameter;

/**
 *  \brief    Fabrik zur Erzeugung konkreter Optionen für den Aufruf von QF-Test über die Kommandozeile
 *
 *  Die Advanced Actions, die im Rahmen dieses Plugins implementiert sind,
 *  rufen QF-Test über die Kommandozeile auf, um bestimmte Aktionen
 *  auszuführen, die von Anwender in NeoLoad parameteriert werden k�nnen.
 *
 *  Da alle Aktionen mindestens ein Kommando absetzen und diese teilweise
 *  �hnlich aufgebaut sind, wurde die Erzeugung der Kommandos aus der
 *  spezialisierten ActionEngine in eine Fabrik gem�� der Entwurfsmuster
 *  nach GOF ausgelagert.
 *
 *  Diese Fabrikklasse bietet die statische Fabrikmethode an, die dann die
 *  Erzeugung einer Liste von Parametern für die Kommandozeile an die
 *  geeigneten Arbeiter delegiert.
 *
 *  @author   dirk.schweier
 *  @since    0.1.0
 */
public class QFTestCommandFactory
{
  /**
   *  \brief  Statische Fabrikmethode, die die Erzeugung an einen geeigneten Arbeiter delegiert
   *
   *  Diese Fabrikmethode kennt die konkreten Arbeiter und reicht den Aufruf
   *  mit der Liste \b parameters an den geeigneten Arbeiter weiter.
   *
   *  @param  type            In diesem Parameter wird das Schlüsselwort für
   *                          das zu erstellende Kommando erwartet.
   *  @param  parameters      In diesem Parameter werden die Informationen
   *                          übergeben, mit denen der Anwender über NeoLoad
   *                          die Advanced Action konfiguriert hat.
   *
   *  @return Die Methode liefert im Rückgabewert eine Liste von Optionen, mit
   *          denen dann später der Kommandozeilenaufruf von QF-Test erzeugt
   *          wird.<br/>
   *          Wenn die Aktion, die im Parameter \b type
   */
  public static List<String> buildCommand(String type, List<ActionParameter> parameters)
  {
    List<String> retval = null;
    if(QFTestPingAction.TYPE.equals(type))
      retval = QFTestCommandWorkerPing.buildCommand(parameters);
    if(QFTestStartAction.TYPE.equals(type))
      retval = QFTestCommandWorkerStartDaemon.buildCommand(parameters);
    if(QFTestStopAction.TYPE.equals(type))
      retval = QFTestCommandWorkerStopDaemon.buildCommand(parameters);
    if(QFTestExecuteAction.TYPE.equals(type))
      retval = QFTestCommandWorkerTestCallDaemon.buildCommand(parameters);
    return retval;
  }
}
