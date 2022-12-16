package org.dschweie.neoload.advancedactions.qf_test.daemon;

import java.util.ArrayList;
import java.util.List;

import org.dschweie.neoload.advancedactions.qf_test.AbstractQFTestAction;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;

/**
 *  \brief      Verwaltungs-Klasse für die Advanced Action zum Ping auf einen QF-Test-Daemon
 *
 *  Diese Klasse ist die Schnittstelle, über die NeoLoad Informationen zu der
 *  angebotenen Advanced Action anfragen kann.
 *
 *  @author     dirk.schweier
 */
public final class QFTestPingAction extends AbstractQFTestAction {

  public static final String TYPE = "QF-Test-Daemon-Ping";

  /**
   *  \brief    Klassenkonstante für die Ressourcendatei
   *
   *  In der Klassenkonstante ist der Pfad zur Ressourcendatei
   *  der Action hinterlegt. Diese Information wird bei der Instanzierung
   *  der Klasse in die Instanzvariable übernommen.
   */
	private static final String BUNDLE_NAME = "org.dschweie.neoload.advancedactions.qf_test.qf-test-daemon-ping";

  /**
   *  \brief  Standardkonstruktor der Klasse
   */
	public QFTestPingAction()
	{
	  super(BUNDLE_NAME);
	}

  //! \copydoc AbstractQFTestAction::getDefaultActionParameters()
	@Override
	public List<ActionParameter> getDefaultActionParameters() {
    final List<ActionParameter> actionParameters = new ArrayList<ActionParameter>();
    actionParameters.add(new ActionParameter("daemonport", "3543"));
    actionParameters.add(new ActionParameter("daemonhost", "localhost"));
    return actionParameters;
	}

  //! \copydoc AbstractQFTestAction::getEngineClass()
	@Override
	public Class<? extends ActionEngine> getEngineClass() {
		return QFTestPingActionEngine.class;
	}

}
