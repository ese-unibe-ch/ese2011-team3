package utilities;

import java.util.Date;

import models.Event;

public enum RepeatableType {
	DAILY {
		public boolean happensOnDay(Event event, Date aDay) {
			// TODO
			return false;
		}
	},

	WEEKLY {
		public boolean happensOnDay(Event event, Date aDay) {
			// TODO
			return false;
		}
	},

	MONTHLY {
		public boolean happensOnDay(Event event, Date aDay) {
			// TODO
			return false;
		}
	},

	YEARLY {
		public boolean happensOnDay(Event event, Date aDay) {
			// TODO
			return false;
		}
	},

	NONE {
		public boolean happensOnDay(Event event, Date aDay) {
			return false;
		}
	};

	public abstract boolean happensOnDay(Event event, Date aDay);

}