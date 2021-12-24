package model;

public enum Category {
	ACTRESS_LR, ACTOR_LR, ACTRESS_SR, ACTOR_SR, BEST_PICTURE, DIRECTING;

	public String toString(){
		switch(this){
		case ACTRESS_LR: return "Actress -- Leading Role";
		case ACTOR_LR: return "Actor -- Leading Role";
		case ACTRESS_SR: return "Actress -- Supporting Role";
		case ACTOR_SR: return "Actor -- Supporting Role";
		case BEST_PICTURE: return "Best Picture";
		case DIRECTING:  return "Directing";
		default: return "unknown";
		}
	}
}
