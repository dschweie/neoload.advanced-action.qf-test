package org.dschweie.neoload.advancedactions.qf_test;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.google.common.base.Optional;
import com.neotys.extensions.action.Action;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;

/**
 *  \brief    Allgemeine Klasse in der Funktionen einer Action zusammengefasst sind
 *  
 *  Eine Advanced Action in NeoLoad besteht aus zwei Klassen:
 *  \li   einer Implementierung der Schnittstelle Action und
 *  \li   einer Implementierung der Schnittstelle ActionEngine.
 *  
 *  über die Klasse, die die Schnittstelle Action implementiert, gelangt NeoLoad
 *  an folgende Informationen zum Umgang mit der Advanced Action:
 *  \li   einen erforderlichen \c String zur Typisierung
 *  \li   eine Pfadangabe, wo die Advanced Action unter den Aktionen angezeigt werden soll
 *  \li   eine Liste der ActionParameter, die standardmäßig angezeigt werden
 *  \li   optional kann ein Icon angegeben werden
 *  \li   optional kann eine Beschreibung hinterlegt werden, die dem Anwender
 *        in NeoLoad angezeigt wird.   
 *  
 *  @author   dirk.schweier
 */
public abstract class AbstractQFTestAction implements Action
{
  /**
   *  \brief    In der Variablen wird der Pfad zur Ressourcendatei gespeichert
   *  
   *  Die Informationen zu einer Action lassen sich in einer Ressourcendatei
   *  hinterlegen. Damit ist es möglich, die Informationen sprachenabhängig zu
   *  speichern.
   */
  protected String strBundle;

  /**
   *  \brief    Bereitstellung eines Konstruktors ohne Parameter
   *  
   *  \attention  Dieser Konstruktor sollte von Spezialisierungen nicht genutzt
   *            werden, da ansonsten die Instanz mit keiner Ressourcen-Datei 
   *            verbunden ist. Dieses kann zu Fehlern in NeoLoad führen!
   *            Der Standardkostruktor der Klasse ist 
   *            AbstractQFTestAction.AbstractQFTestAction(String)
   */
  public AbstractQFTestAction()
  {
    this(null);
  }
  
  /**
   *  \brief    Standardkonstruktor, der die Instanzvariablen mit Werten versorgt
   *  
   *  Dieser Konstruktor ist Standardkonstruktor dieser Klasse und belegt
   *  die Instanzvariable \c bundle mit einem Wert vor, der über den Parameter
   *  mitgegeben wird.
   *  
   *  @param    bundle        In dem Parameter hat der Aufrufer einen 
   *                          gültigen Pfad zur Ressourcen-Datei zu übergeben.   
   */
  public AbstractQFTestAction(String bundle)
  {
    this.strBundle = bundle;
  }
  
  /**
   *  \brief    Getter-Methode liefert die Standardparameter der Action
   *  
   *  Die Methode liefert den Satz an Parametern, die dem Anwender in NeoLoad
   *  gezeigt werden, wenn er die Action in einen User Path übernimmt.
   *  
   *  \note     Die Spezialisierungen dieser Klasse bestimmen den Umfang der
   *            Parameter. Es wird empfohlen, dass mindestens das Set der 
   *            erforderlichen Parameter zurückgegeben wird.
   *  
   *  \return   Die Methode liefert eine Liste der Parameter zurück, die
   *            leer sein kann, aber nicht \c null .
   */
  @Override
  abstract public List<ActionParameter> getDefaultActionParameters();
  
  /**
   *  \brief    Getter-Methode liefert die Klasse zur Ausführung der Action
   *  
   *  über diese Getter-Methode bekommt der Aufrufer den Namen der Klasse,
   *  die instanziert werden muss, um eine Action auszuführen.
   *  
   *  \return   Die Methode liefert die Klasse, die geeignet ist, die
   *            Advanced Action auszuführen.
   */
  @Override
  abstract public Class<? extends ActionEngine> getEngineClass();
  
  /**
   *  \brief    Getter-Methode liefert die minimale NeoLoad-Version für die Action
   *  
   *  Für eine Advanced Action kann parametriert werden, für welche Versionen 
   *  von NeoLoad die Advanced Action gültig sein soll. Nur in diesem Versionen
   *  wird die Advanced Action dann auch angeboten.
   *  
   *  Diese Getter-Methode liefert den Wert für die minimale Version von 
   *  NeoLoad.
   *  
   *  \return   Die Methode liefert eine Instanz der Klasse Optional mit der
   *            entsprechenden Versionskennung.
   */
  @Override
  public Optional<String> getMinimumNeoLoadVersion()
  {
    String version = ResourceBundle.getBundle(this.strBundle, Locale.getDefault()).getString("maximumVersion");
    return (Optional<String>) (version.equals("absent")?Optional.absent():Optional.of(version));
  }

  /**
   *  \brief    Getter-Methode liefert die maximale NeoLoad-Version für die Action
   *  
   *  Für eine Advanced Action kann parametriert werden, für welche Versionen 
   *  von NeoLoad die Advanced Action gültig sein soll. Nur in diesem Versionen
   *  wird die Advanced Action dann auch angeboten.
   *  
   *  Diese Getter-Methode liefert den Wert für die maximale Version von 
   *  NeoLoad.
   *  
   *  \return   Die Methode liefert eine Instanz der Klasse Optional mit der
   *            entsprechenden Versionskennung.
   */
  @Override
  public Optional<String> getMaximumNeoLoadVersion()
  {
    String version = ResourceBundle.getBundle(this.strBundle, Locale.getDefault()).getString("maximumVersion");
    return (Optional<String>) (version.equals("absent")?Optional.absent():Optional.of(version));
  }

  /**
   *  \brief    Getter-Methode liefert die Information, ob Action standardmäßig als Hit gewertet wird
   *  
   *  NeoLoad versteht unter dem Hit etwas, das als Request gegen das SUT 
   *  verstanden werden kann und somit auch eine Antwortzeit hat. Wenn eine 
   *  Action \c true liefert, dann wird die Ausführung der Action in die 
   *  Statistik der Requests und Antwortzeiten aufgenommen.
   *  
   *  Diese Getter-Methode liefert die Information, ob die Action per Default
   *  als Hit zu werten ist oder nicht. 
   *  
   *  \return   Da ein QF-Test-Skript eine Vielzahl an Interaktionen 
   *            mit dem SUT hat, wird false zurückgegeben. 
   */
  @Override
  public boolean getDefaultIsHit()
  {
    return false;
  }

  /**
   *  \brief    Getter-Methode liefert den Typ der Action
   *  
   *  Die Getter-Methode liest den Typ der Datei sprachenabhängig aus der
   *  zugehörigen Ressourcen-Datei aus.
   *  
   *  @return   Die Methode liefert den Typ als String zurück.
   */
  @Override
  public String getType()
  {
    return ResourceBundle.getBundle(this.strBundle, Locale.getDefault()).getString("displayName");
  }

  /**
   *  \brief    Getter-Methode für ein 16x16 Icon, dass in NeoLoad für die Action verwendet werden soll
   *  
   *  \return   Die Methode liefert eine Instanz der Klasse Icon zurück. Als 
   *            Icon wird die Ressource genutzt, die unter \c iconPath
   *            hinterlegt ist.
   */
  @Override
  public Icon getIcon()
  {
    return new ImageIcon( this.getClass().getResource(ResourceBundle.getBundle(this.strBundle, Locale.getDefault()).getString("iconPath")),
        ResourceBundle.getBundle(this.strBundle, Locale.getDefault()).getString("type")       );
  }
  
  /**
   *  \brief    Getter-Methode für die Beschreibung der Action
   *  
   *  \return   Die Methode liefert die Beschreibung, die in der entsprechenden
   *            Ressorcendatei hinterlegt ist als String zurück. 
   */
  @Override
  public String getDescription()
  {
    return ResourceBundle.getBundle(this.strBundle, Locale.getDefault()).getString("description");
  }

  /**
   *  \brief    Getter-Methode für die Anzeigetext der Action
   *  
   *  \return   Die Methode liefert den Anzeigetext, die in der entsprechenden
   *            Ressorcendatei unter \c displayName hinterlegt ist als String
   *            zurück. 
   */
  @Override
  public String getDisplayName()
  {
    return ResourceBundle.getBundle(this.strBundle, Locale.getDefault()).getString("displayName");
  }

  /**
   *  \brief    Getter-Methode für den Pfad, unter dem die Action in NeoLoad zu finden ist
   *  
   *  Advanced Actions werden in NeoLoad in dem Bereich Actions angezeigt.
   *  Mit Hilfe einer Pfadangaben lassen sich die Actions in einer Baumstruktur 
   *  gliedern. Dabei dient \c / als Trennzeichen für Ordner.
   *  
   *  \return   Die Getter-Methode liefert den Wert von \c displayPath aus der 
   *            Ressourendatei als String.  
   */
  @Override
  public String getDisplayPath()
  {
    return ResourceBundle.getBundle(this.strBundle, Locale.getDefault()).getString("displayPath");
  }


}
