package com.volumetricpixels.bans.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import org.json.JSONException;
import org.json.JSONObject;

import com.volumetricpixels.bans.exception.StorageException;

public class JSONFileHandler {
	private File file;

	private BufferedWriter writer;
	private BufferedReader reader;

	public JSONFileHandler(File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public void startWriting() throws StorageException {
		try {
			if (!file.exists()) {
				create();
			}
			writer = new BufferedWriter(new FileWriter(file));
		} catch (IOException e) {
			throw new StorageException("Could not start writing to file!", e);
		}
	}

	public void stopWriting() throws StorageException {
		try {
			writer.flush();
			writer.close();
			writer = null;
		} catch (IOException e) {
			throw new StorageException("Could not flush or close file!", e);
		}
	}

	public void write(JSONObject jO) throws StorageException {
		try {
			writer.write(jO.toString());
			writer.newLine();
		} catch (IOException e) {
			throw new StorageException("Could not write JSONObject to file (or could not add new line)!", e);
		}
	}

	public void startReading() throws StorageException {
		try {
			if (!file.exists()) {
				create();
			}
			reader = new BufferedReader(new FileReader(file));
		} catch (IOException e) {
			throw new StorageException("Could not start reading from file!", e);
		}
	}

	public void stopReading() throws StorageException {
		try {
			reader.close();
			reader = null;
		} catch (IOException e) {
			throw new StorageException("Could not close BufferedReader!", e);
		}
	}

	public JSONObject read() throws StorageException {
		try {
			String line = reader.readLine();
			if ((line != null) && !line.equals("")) {
				return new JSONObject(line);
			} else {
				return null;
			}
		} catch (JSONException e) {
			throw new StorageException("Could not create JSONObject from line!", e);
		} catch (IOException e) {
			throw new StorageException("Could not read line from file!", e);
		}
	}

	public void backup() throws StorageException {
		try {
			File bck = new File(file.getAbsolutePath() + ".bck");
			bck.delete();
			FileUtils.copyFile(file, bck);
		} catch (IOException e) {
			throw new StorageException("Could not back up file!", e);
		}
	}

	public void delete() {
		file.delete();
	}

	public void create() throws StorageException {
		try {
			file.createNewFile();
		} catch (IOException e) {
			throw new StorageException("Could not create file!", e);
		}
	}
}
