package university.interfaces;

import university.models.ResearchPaper;

public interface Subscriber {
    void update(String journalName, ResearchPaper paper);
}
