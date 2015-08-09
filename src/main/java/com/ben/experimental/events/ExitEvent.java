package com.ben.experimental.events;

import com.ben.experimental.events.interfaces.ControllerEvent;
import com.ben.experimental.events.interfaces.DisplayEvent;
import com.ben.experimental.events.interfaces.PollerEvent;

/**
 * Created by Ben on 8/4/2015.
 */
public class ExitEvent extends AbstractEvent implements ControllerEvent, DisplayEvent, PollerEvent {
}
