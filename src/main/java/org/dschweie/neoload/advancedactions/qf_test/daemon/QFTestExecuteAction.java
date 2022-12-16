package org.dschweie.neoload.advancedactions.qf_test.daemon;

import java.util.ArrayList;
import java.util.List;

import org.dschweie.neoload.advancedactions.qf_test.AbstractQFTestAction;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;

/**
 *  \brief    Verwaltungs-Klasse für die Advanced Action zur Ausführung eines Testskripts durch QF-Test
 *
 *  Diese Klasse repräsentiert eine Implementierung der AbstractQFTestAction
 *  mit der ein Testskript über einen QF-Test Daemon zur Ausführung gebracht
 *  werden kann.
 *
 *  Diese Klasse ist die Schnittstelle, über die NeoLoad Informationen zu der
 *  angebotenen Advanced Action anfragen kann.
 *
 *  @author   dirk.schweier
 */
public class QFTestExecuteAction extends AbstractQFTestAction
{
  public static final String TYPE = "QF-Test-Daemon-Execute";
  /**
   *  \brief    Klassenkonstante für die Ressourcendatei
   *
   *  In der Klassenkonstante ist der Pfad zur Ressourcendatei
   *  der Action hinterlegt. Diese Information wird bei der Instanzierung
   *  der Klasse in die Instanzvariable übernommen.
   */
  private static final String BUNDLE_NAME = "org.dschweie.neoload.advancedactions.qf_test.qf-test-daemon-execute";

  /**
   *  \brief  Standardkonstruktor der Klasse
   */
  public QFTestExecuteAction()
  {
    super(BUNDLE_NAME);
  }

  //! \copydoc AbstractQFTestAction::getDefaultActionParameters()
  @Override
  public List<ActionParameter> getDefaultActionParameters()
  {
    final List<ActionParameter> actionParameters = new ArrayList<ActionParameter>();

    actionParameters.add(new ActionParameter("daemonport", "3543"));
    actionParameters.add(new ActionParameter("daemonhost", "localhost"));
    actionParameters.add(new ActionParameter("suitedir", "${NL-CustomResources}"));
    actionParameters.add(new ActionParameter("runlog", "<qualified filename>"));
    actionParameters.add(new ActionParameter("testcase", "suite#testcase"));
    actionParameters.add(new ActionParameter("variable", "name=value"));

    return actionParameters;
  }

  //! \copydoc AbstractQFTestAction::getEngineClass()
  @Override
  public Class<? extends ActionEngine> getEngineClass()
  {
    return QFTestExecuteActionEngine.class;
  }

}
