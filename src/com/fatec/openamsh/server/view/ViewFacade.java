package com.fatec.openamsh.server.view;

public class ViewFacade {

	private View view;
	
	public ViewFacade() {
		view = createView();
	}
	
	protected View createView() {
		return new ViewFile();
	}
	
	public void message(String message) {
		view.message(message);
	}
}
