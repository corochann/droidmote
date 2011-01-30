package cz.mpelant.droidmotepc;

/**
 * This is a Server superclass which both TCP and UDP listener inherits from.
 */
public abstract class Server implements Runnable {

	/** The port. */
	protected int port;

	/** The gui for LOGs. */
	protected MainWindow gui;

	/** Indicates if the listener is running. */
	protected boolean running = false;

	/** The stop. */
	protected boolean stop = false;

	/** The thread. */
	protected Thread thread;

	/** The keys. */
	protected Keys keys;

	/**
	 * Instantiates a new server.
	 * 
	 * @param gui the gui
	 */
	public Server(MainWindow gui) {
		this.gui = gui;
		keys = new Keys();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
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

	/**
	 * Do in background. This method is to be overridden by descendants It is called by the run method which means that it runs in the new thread.
	 * 
	 * @throws Exception the exception
	 */
	protected abstract void doInBackground() throws Exception;

	/**
	 * Close socket.
	 */
	public abstract void closeSocket();

	/**
	 * Checks if the listener is running.
	 * 
	 * @return true, if it is running
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Stop the listener. interrupts and closes the socket.
	 */
	public void stop() {
		if (!isRunning())
			return;
		stop = true;
		try {
			thread.interrupt();
			closeSocket();
		} catch (Exception e) {
			System.out.println("exception " + e.toString());
		}
	}

	/**
	 * Start.
	 */
	protected void start() {
		thread = new Thread(this);
		thread.start();
	}

}
