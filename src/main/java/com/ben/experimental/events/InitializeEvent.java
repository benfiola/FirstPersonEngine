package com.ben.experimental.events;

import com.ben.experimental.events.interfaces.DisplayEvent;
import com.ben.experimental.events.interfaces.ControllerEvent;
import com.ben.experimental.events.interfaces.PollerEvent;
import com.ben.experimental.threads.DisplayThread;

/**
 * Created by Ben on 8/5/2015.
 */
public class InitializeEvent extends AbstractEvent implements DisplayEvent, ControllerEvent, PollerEvent {
}
