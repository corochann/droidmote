package cz.mpelant.droidmotepc;

public abstract class Server implements Runnable {
	protected int port;
	protected MainWindow gui;
	protected boolean running = false;
	protected boolean stop = false;
	protected Thread thread;
	protected Keys keys;

	public Server(MainWindow gui) {
		this.gui = gui;
		keys = new Keys();
	}

	@Override
	public void run() {
		stop = false;
		running = true;
		gui.updateButtonText();
		try {
			doInBackground();
		} catch (Exception e) {
		} finally {
			running = false;
			closeSocket();
			gui.updateButtonText();
		}
	}

	protected abstract void doInBackground() throws Exception;

	public abstract void closeSocket();

	public boolean isRunning() {
		return running;
	}

	public void stop() {
		if(!isRunning())
			return;
		stop = true;
		try {
			thread.interrupt();
			closeSocket();
		} catch (Exception e) {
			System.out.println("exception " + e.toString());
		}
	}

	protected void start() {
		thread = new Thread(this);
		thread.start();
	}

}
