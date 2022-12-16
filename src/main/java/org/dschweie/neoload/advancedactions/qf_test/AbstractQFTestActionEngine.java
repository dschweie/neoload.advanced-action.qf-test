package org.dschweie.neoload.advancedactions.qf_test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.SampleResult;

/**
 *  @brief  Allgemeine Implementierung für die Ausführung einer Action
 *    
 *  Die Klasse implementiert mit ActionEngine das Interface ActionEngine,
 *  das von NeoLoad gefordert wird, damit NeoLoad eine Advanced Action 
 *  aufrufen und stoppen kann. 
 *  
 *  @author dirk.schweier
 *  
 */
public abstract class AbstractQFTestActionEngine implements ActionEngine
{
  /**
   *  \brief    Die statische Methode erzeugt Fehlereinträge in NeoLoad und liefert ein entscprechendes Ergebnis
   *  
   *  über diese Methode lässt sich ein Fehler, der in der Verarbeitung der 
   *  Advanced Action aufgetreten ist, in NeoLoad protokollieren und zusätzlich
   *  wird ein SampleResult erzeugt, das als Rückgabewert der Advanced Action
   *  genutzt werden kann.  
   *  
   *  @param    context       In dem Parameter ist eine Instanz zu übergeben,
   *                          über die die Methode Zugriff auf Informationen
   *                          von NeoLoad zur Laufzeit hat.
   *  @param    result        In dem Parameter wird die aktuelle Instanz von
   *                          SampleResult erwartet, die von der 
   *                          execute-Methode an NeoLoad zurückgegeben werden
   *                          soll.
   *  @param    code          In dem Parameter ist der Fehlercode zu übergeben,
   *                          der im Ergebnis eingetragen werden soll. Dieser 
   *                          Fehlercode ist frei w�hlbar und als String zu
   *                          übergeben.
   *  @param    errorMessage  In dem Parameter ist eine Fehlermeldung zu 
   *                          übergeben, die zusammen mit dem Fehlercode 
   *                          protokolliert werden soll.
   *  @param    exception     In dem Parameter kann optional eine Exception
   *                          übergeben werden, die im Zusammenhang mit der
   *                          Verarbeitung gefangen wurde.
   *                          
   *  @return   Die Methode liefert aus Gr�nden des Komfort die ge�nderte 
   *            Instanz \em result zurück.
   */
  protected static SampleResult getErrorResult(final Context context, final SampleResult result, final String code, final String errorMessage, final Exception exception) {
    result.setError(true);
    result.setStatusCode(code);
    //result.setResponseContent(errorMessage);
    if(exception != null){
      if(null != context)        
        context.getLogger().error(errorMessage, exception);
      else
        System.out.println(errorMessage.concat("\n\n").concat(exception.getLocalizedMessage()));
    } else{
      if(null != context)        
        context.getLogger().error(errorMessage);
      else
        System.out.println(errorMessage);
    }
    return result;
  }
  
  /**
   *  \brief    Instanzvariable für den Zugriff auf den gestarteten Prozess
   *  
   *  Diese abstrakte Klasse startet eine Applikation über einen Aufruf
   *  über die Kommandozeile. In dieser Instanzvariable wird eine Referenz
   *  auf den Prozess gehalten.  
   */
  protected Process       process = null;

  /**
   *  \brief    Instanzvariable, in der Informationen für das Ergebnis der Action gehalten werden
   */
  protected SampleResult  sampleResult = new SampleResult();

  /**
   *  \brief    Instanzvariable, in der Informationen zum Aufruf des Prozesses gehalten werden
   */
  protected StringBuilder requestBuilder = new StringBuilder();
  
  /**
   *  \brief    Instanzvariable, in der Ausgaben des Prozesses gehalten werden
   */
  protected StringBuilder responseBuilder = new StringBuilder();
 
 
  /**
   *  \brief    Standardkonstruktor der für Engine-Klassen
   *  
   *  Bei dieser Klasse handelt es sich um eine möglichst allgemeine
   *  Implementierung einer Engine-Klasse, die zur Ausführung einer Advanced
   *  Action über AbstractQFTestActionEngine.execute(Context, List<ActionParameter>)
   *  aufgerufen wird.
   *  
   *  Der Standardkonstruktor instanziert die Instanzvariablen, die 
   *  zur Ausgabe von
   *    \li   Request-Content,
   *    \li   Response-Content und
   *    \li   Ergebnis der Action verwendet werden.
   */
  public AbstractQFTestActionEngine()
  {
    this.sampleResult = new SampleResult();
    this.requestBuilder = new StringBuilder();
    this.responseBuilder = new StringBuilder();
  }

  /**
   *  \brief    Methode erzeugt aus einer Liste von Strings das Kommando
   *  
   *  Die Methode baut aus einer Liste einzelner Strings, die für Parameter 
   *  und deren Werte stehen, das Kommando auf, das über die Komandozeile 
   *  aufgerufen werden soll.
   *  
   *  \return   Die Methode liefert das Kommando als String zurück.
   */
  protected String buildProcessCallToString(List<String> command)
  {
    String str = "";
    
    for(int i = 0; i < command.size(); ++i)
      if(0 < i)
        str = str.concat(" ".concat(command.get(i)));
      else
        str = str.concat(command.get(i));
   
    return str;
  }

  /**
   *  \brief    Methode zur Ausführung eines QF-Test-Kommandos über die Kommandozeile
   *  
   *  Da eine Abarbeitung eines QF-Test-Kommandos im Wesentlichen gleich ist, 
   *  stellt die abstrakte Klasse mit dieser Methode eine allgemeine Methode
   *  bereit.
   *  
   *  Diese Methode führt selbst keine Verarbeitung durch, sondern ruft
   *  AbstractQFTestActionEngine.executeProcess(Context, List<String>, boolean, boolean)
   *  auf und setzt die beiden boolschen Parameter auf den Wert \c true . 
   *  
   *  @param    context       In dem Parameter ist eine Instanz zu übergeben,
   *                          über die die Methode Zugriff auf Informationen
   *                          von NeoLoad zur Laufzeit hat.
   *                          Die Instanz wird von NeoLoad über die Methode 
   *                          ActionEngine.execute(Context, List<ActionParameter>)
   *                          übergeben.     
   *  @param    command       In diesem Parameter wird eine Liste erwartet, 
   *                          in der die Bestandteile des Kommandos enthalten 
   *                          sind.
   *                          
   *  @return   Die Methode liefert das Ergebnis der Ausführung als Instanz der
   *            Klasse SampleRequest zurück. Diese Instanz wird von NeoLoad
   *            als Rückmeldung erwartet.
   *  
   *  \see      AbstractQFTestActionEngine.executeProcess(Context, List<String>, boolean, boolean)
   */
  protected SampleResult executeProcess(Context context, List<String> command)
  {
    return this.executeProcess(context, command, true, true);
  }
  
  /**
   *  \brief    Methode zur Ausführung eines QF-Test-Kommandos über die Kommandozeile
   *  
   *  Da eine Abarbeitung eines QF-Test-Kommandos im Wesentlichen gleich ist, 
   *  stellt die abstrakte Klasse mit dieser Methode eine allgemeine Methode
   *  bereit.
   *  
   *  @param    context       In dem Parameter ist eine Instanz zu übergeben,
   *                          über die die Methode Zugriff auf Informationen
   *                          von NeoLoad zur Laufzeit hat.
   *                          Die Instanz wird von NeoLoad über die Methode 
   *                          ActionEngine.execute(Context, List<ActionParameter>)
   *                          übergeben.     
   *  @param    command       In diesem Parameter wird eine Liste erwartet, 
   *                          in der die Bestandteile des Kommandos enthalten 
   *                          sind.
   *  @param    waitForProcess  Mit diesem Parameter wird gesteuert, ob die 
   *                          Methode auf das Ende der Verarbeitung des 
   *                          Kommandos warten soll.
   *  @param    isMainProcess über diesen Parameter wird gesteuert, ob das
   *                          aufgerufene Kommando als Hauptprozess der Action
   *                          interpretiert werden soll.
   *                          
   *  @return   Die Methode liefert das Ergebnis der Ausführung als Instanz der
   *            Klasse SampleRequest zurück. Diese Instanz wird von NeoLoad
   *            als Rückmeldung erwartet.
   */
  protected SampleResult executeProcess(Context context, List<String> command, boolean waitForProcess, boolean isMainProcess)
  {
    SampleResult result = new SampleResult();
    Process currentProcess = null;
        
    // log the concrete call
    this.reportProcessCall(command);
    if(isMainProcess)
      this.reportToResponse("<?xml version=\"1.0\"?>");
    
    try
    {
      // run action as a external process
      result.sampleStart();
      currentProcess = new ProcessBuilder(command).start();
      if(isMainProcess)
        this.process = currentProcess;
      if(waitForProcess)
        currentProcess.waitFor();
      result.sampleEnd();
      
      // log the results
      if(waitForProcess)
        result.setStatusCode(String.valueOf(currentProcess.exitValue()));
      else
      { //  in this case the end of process will not be observed, so exit code is set to 0 
        result.setStatusCode("0");
      }
      this.reportProcessInput(context, currentProcess);
      this.responseBuilder.append("\n<exitcode>".concat(result.getStatusCode()).concat("</exitcode>"));
    }
    catch (IOException e1)
    { 
      result.sampleEnd();
      result.setStatusCode(e1.getClass().getSimpleName());
      this.reportProcessInput(context, currentProcess);
      this.responseBuilder.append("<exception>".concat(e1.getLocalizedMessage()).concat("</exception>"));
    }
    catch (InterruptedException e)
    {
      result.sampleEnd();
      result.setStatusCode(e.getClass().getSimpleName());
      this.reportProcessInput(context, currentProcess);
      this.responseBuilder.append("<exception>".concat(e.getLocalizedMessage()).concat("</exception>"));
    }

    // update the result object
    result.setError(!("0".equals(result.getStatusCode())));
    if(result.isError())
      this.reportProcessErrors(context, currentProcess);
    result.setRequestContent(this.requestBuilder.toString());
    result.setResponseContent(this.responseBuilder.toString());
    
    if(null == context)
      System.out.println(result.getRequestContent().concat(" => ").concat(result.getStatusCode()));

    return result;
  }
  
  /**
   *  \brief    Methode zur Verabeitung eines Kommandos als Unterprozess
   *  
   *  Komplexe Actions bestehen aus der Ausführung von mehr als einem Kommando.
   *  In diesem Fall wird ein Kommando als Hauptprozess verstanden und die 
   *  weiteren Kommandos als Unterprozesse.
   *  
   *  Diese Methode verarbeitet eine Kommando als Unterprozess und ruft dazu 
   *  die Methode 
   *  AbstractQFTestActionEngine.executeProcess(Context, List<String>, boolean, boolean)
   *  auf und übergibt im letzten Parameter den Wert \c false . Im vorletzten
   *  Parameter wird \c true übergeben, was dazu führt, dass auf das Ende
   *  des Prozess gewartet wird.
   *   
   *  @param    context       In dem Parameter ist eine Instanz zu übergeben,
   *                          über die die Methode Zugriff auf Informationen
   *                          von NeoLoad zur Laufzeit hat.
   *                          Die Instanz wird von NeoLoad über die Methode 
   *                          ActionEngine.execute(Context, List<ActionParameter>)
   *                          übergeben.     
   *  @param    command       In diesem Parameter wird eine Liste erwartet, 
   *                          in der die Bestandteile des Kommandos enthalten 
   *                          sind.
   *                          
   *  @return   Die Methode liefert das Ergebnis der Ausführung als Instanz der
   *            Klasse SampleRequest zurück. Diese Ergbenis ist mit den anderen
   *            Ergebnissen aus dem Hauptprozess zu einem zusammenzufassen.
   *  
   *  \see      AbstractQFTestActionEngine.executeProcess(Context, List<String>, boolean, boolean)
   */
  protected SampleResult executeSubprocess(Context context, List<String> command)
  {
    return this.executeProcess(context, command, true, false);
  }

  /**
   *  \brief    Methode zur Verabeitung eines Kommandos als Unterprozess
   *  
   *  Komplexe Actions bestehen aus der Ausführung von mehr als einem Kommando.
   *  In diesem Fall wird ein Kommando als Hauptprozess verstanden und die 
   *  weiteren Kommandos als Unterprozesse.
   *  
   *  Diese Methode verarbeitet eine Kommando als Unterprozess und ruft dazu 
   *  die Methode 
   *  AbstractQFTestActionEngine.executeProcess(Context, List<String>, boolean, boolean)
   *  auf und übergibt im letzten Parameter den Wert \c false .
   *   
   *  @param    context       In dem Parameter ist eine Instanz zu übergeben,
   *                          über die die Methode Zugriff auf Informationen
   *                          von NeoLoad zur Laufzeit hat.
   *                          Die Instanz wird von NeoLoad über die Methode 
   *                          ActionEngine.execute(Context, List<ActionParameter>)
   *                          übergeben.     
   *  @param    command       In diesem Parameter wird eine Liste erwartet, 
   *                          in der die Bestandteile des Kommandos enthalten 
   *                          sind.
   *  @param    waitForProcess  Mit diesem Parameter wird gesteuert, ob die 
   *                          Methode auf das Ende der Verarbeitung des 
   *                          Kommandos warten soll.
   *                          
   *  @return   Die Methode liefert das Ergebnis der Ausführung als Instanz der
   *            Klasse SampleRequest zurück. Diese Ergbenis ist mit den anderen
   *            Ergebnissen aus dem Hauptprozess zu einem zusammenzufassen.
   *  
   *  \see      AbstractQFTestActionEngine.executeProcess(Context, List<String>, boolean, boolean)
   */
  protected SampleResult executeSubprocess(Context context, List<String> command, boolean waitForProcess)
  {
    return this.executeProcess(context, command, waitForProcess, false);
  }
  
  /**
   *  \brief    Reporting-Methode, die ein Kommando in die Request-Ausgabe protokolliert
   *  
   *  Eine Advanced Action bestet in der Darstellung in der Laufzeitumgebung 
   *  von NeoLoad aus einem Request und einer Response.
   *  
   *  Der Aufruf eines Kommandos wird als Request interpretiert und mit dieser
   *  Methode wird der Aufruf protokolliert. Damit kann der Anwender 
   *  nachvollziehen, wie genau der Aufruf zur Laufzeit aussieht.
   *  
   *  @param    command       In diesem Parameter wird eine Liste erwartet, 
   *                          in der die Bestandteile des Kommandos enthalten 
   *                          sind.
   */
  protected void reportProcessCall(List<String> command)
  {
    if(0 < this.requestBuilder.length())
      this.requestBuilder.append("\n");
    this.requestBuilder.append(this.buildProcessCallToString(command));
  }
  
  /**
   *  \brief    Reporting-Methode, die Fehler aus dem Prozess direkt in NeoLoad protokolliert
   *  
   *  Die Fehler, die im Zusammenhang mit der Verarbeitung eines Kommandos 
   *  aufgetreten sind, werden mit Hilfe dieser Methode direkt in die 
   *  Laufzeitumgebung von NeoLoad in das Logfile geschrieben.
   *  
   *  Zusätzlich wird die Fehlermeldung in der Response festgehalten.
   *  
   *  @param    context       In dem Parameter ist eine Instanz zu übergeben,
   *                          über die die Methode Zugriff auf Informationen
   *                          von NeoLoad zur Laufzeit hat.
   *                          Die Instanz wird von NeoLoad über die Methode 
   *                          ActionEngine.execute(Context, List<ActionParameter>)
   *                          übergeben.     
   *  @param    process       In dem Parameter ist die Instanz zu übergeben,
   *                          die die Ausführung eines Kommandos repräsentiert
   *                          und dessen Fehler protokolliert werden sollen.
   */
  protected void reportProcessErrors(Context context, Process process)
  {
    String message = "";
    
    // read message from ErrorStream
    InputStream output  = process.getErrorStream();
    try
    {
      int c = output.read();
      while(-1 != c)
      {
        message = message.concat(String.valueOf((char) c));
        c = output.read();
      }
      output.close();
      
      //  write message to reponse of the action
      if(0 < this.responseBuilder.length())
        this.responseBuilder.append("\n");
      this.responseBuilder.append("<errormessage>\n".concat(message).concat("</errormessage>"));
      
      //  write message to logfile of NeoLoad
      if(null!=context)
        context.getLogger().error(message);
      else
        System.out.println("reportProcessErrors: ".concat(message));
    }
    catch (IOException e)
    {
      this.responseBuilder.append("<exception>".concat(e.getLocalizedMessage()).concat("</exception>"));
      context.getLogger().error(message, e);
    }      
  }
  
  /**
   *  \brief    Reporting-Methode zur Protokollierung der Ausgaben über die Kommandozeile
   *  
   *  Der Aufruf von QF-Test über die Kommandozeile führt teilweise zu Ausgaben
   *  in der Konsole. Diese Ausgaben werden von dieser Action als Teil der 
   *  Antwort interpretiert und folglich in der Response protokolliert. 
   *  
   *  @param    context       In dem Parameter ist eine Instanz zu übergeben,
   *                          über die die Methode Zugriff auf Informationen
   *                          von NeoLoad zur Laufzeit hat.
   *                          Die Instanz wird von NeoLoad über die Methode 
   *                          ActionEngine.execute(Context, List<ActionParameter>)
   *                          übergeben.     
   *  @param    process       In dem Parameter ist die Instanz zu übergeben,
   *                          die die Ausführung eines Kommandos repräsentiert
   *                          und dessen Ausgabe über die Kommadozeile 
   *                          protokolliert werden sollen.
   */
  protected void reportProcessInput(Context context, Process process)
  {
    InputStream output  = process.getInputStream();
    try
    {
      if(0 < this.responseBuilder.length())
        this.responseBuilder.append("\n");
      
      this.responseBuilder.append("<console>\n");
      int c = output.read();
      while(-1 != c)
      {
        this.responseBuilder.append((char) c);
        c = output.read();
      }
      output.close();
      this.responseBuilder.append("</console>");
    }
    catch (IOException e)
    {
      this.responseBuilder.append("<exception>".concat(e.getLocalizedMessage()).concat("</exception>"));
    }      
  }
  
  /**
   *  \brief    Reporting-Methode zur Protokollierung eines Texts im Request der Action
   *  
   *  Mit Hilfe dieser Methode kann der Text, der im Parameter \em message 
   *  übergeben wird, in das Logging des Request der Action eingetragen 
   *  werden.
   *  
   *  Der \em message wird, sofern der Request nicht leer ist, ein 
   *  Zeilenumbruch vorangestellt und an den bestehenden Inhalt angehängt. 
   *  
   *  @param    message       In dem Parameter ist der Text zu übergeben, der
   *                          protokolliert werden soll.
   */
  protected void reportToRequest(String message)
  {
    if(0 < this.requestBuilder.length())
      this.requestBuilder.append("\n");
    this.requestBuilder.append(message.toString());
  }
  
  /**
   *  \brief    Reporting-Methode zur Protokollierung eines Texts in der Response der Action
   *  
   *  Mit Hilfe dieser Methode kann der Text, der im Parameter \em message 
   *  übergeben wird, in das Logging des Request der Action eingetragen 
   *  werden.
   *  
   *  Der \em message wird, sofern die Response nicht leer ist, ein 
   *  Zeilenumbruch vorangestellt und an den bestehenden Inhalt angehängt. 
   *  
   *  @param    message       In dem Parameter ist der Text zu übergeben, der
   *                          protokolliert werden soll.
   */
  protected void reportToResponse(String message)
  {
    if(0 < this.responseBuilder.length())
      this.responseBuilder.append("\n");
    this.responseBuilder.append(message.toString());
  }

  /**
   *  \brief    Methode, die von NeoLoad gerufen wird, wenn der Test gestoppt wird
   *  
   *  Der Anwender kann in der Oberfläche von NeoLoad Tests sofort stoppen.
   *  In diesem Fall müssen auch Advanced Actions sofort gestoppt werden.
   *  
   *  In dieser Methode wird der laufende Hauptprozess beendet.
   */
  @Override
  public void stopExecute() 
  {
    this.process.destroy();
  }
}
