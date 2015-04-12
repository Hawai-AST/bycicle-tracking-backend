package de.hawai.bicycle_tracking.server.utility.value;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class FrameNumber
{
	private long number;

	private FrameNumber() {
	}

	public FrameNumber(long inFrameNumber) {
		this.number = inFrameNumber;
	}

	@Column(name = "frame_number", nullable = false)
	public long getNumber()
	{
		return number;
	}

	@Override
	public boolean equals(final Object o)
	{
		if(this == o)
			return true;
		if(o == null || getClass() != o.getClass())
			return false;

		FrameNumber that = (FrameNumber)o;

		return number == that.number;
	}

	@Override
	public int hashCode()
	{
		return (int)(number ^ (number >>> 32));
	}
}
