package com.cogitareforma.makhana.common.eventsystem;

import java.time.LocalDateTime;

public abstract class Event
{
	private LocalDateTime timestamp;

	public Event( )
	{
		this.timestamp = LocalDateTime.now( );
	}

	public LocalDateTime getLocalDateTime( )
	{
		return timestamp;
	}
}
