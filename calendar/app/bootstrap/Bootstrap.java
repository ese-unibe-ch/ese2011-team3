package bootstrap;

import models.User;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

@OnApplicationStart
public class Bootstrap extends Job {

	@Override
	public void doJob() {
		if (User.count() == 0) {
			new User("wuschu", "Domi Jason", "secret", "domi@gmail.com").save();
			new User("lexy", "lex McColin", "secret", "lex@gmail.com").save();
			new User("king_julien", "Julien Peterson", "secret",
					"king@gmail.com").save();
			new User("joe", "Joe Gargamel", "secret", "gargamel@gmail.com")
					.save();
		}
	}

}
