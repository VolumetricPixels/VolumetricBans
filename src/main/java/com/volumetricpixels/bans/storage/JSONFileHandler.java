package com.volumetricpixels.bans.storage;

import java.io.*;

import org.apache.commons.io.FileUtils;

import org.json.JSONException;
import org.json.JSONObject;

import com.volumetricpixels.bans.exception.StorageException;

public final class JSONFileHandler {
    /** The File we are writing to / reading from */
    private final File file;

    /** The BufferedWriter in use */
    private BufferedWriter writer;
    /** The BufferedReader in use */
    private BufferedReader reader;

    /**
     * Creates a new JSONFileHandler with the given File
     * 
     * @param file
     *            The File to write to / read from
     */
    public JSONFileHandler(final File file) {
        this.file = file;
    }

    /**
     * Gets the File we are writing to / reading from
     * 
     * @return This JSONFileHandler's file
     */
    public File getFile() {
        return file;
    }

    /**
     * Gets ready to write to the file
     * 
     * @throws StorageException
     *             When we cannot initialise the BufferedWriter
     */
    public void startWriting() throws StorageException {
        if (writer != null) {
            stopWriting();
        }
        try {
            if (!file.exists()) {
                create();
            }
            writer = new BufferedWriter(new FileWriter(file));
        } catch (final IOException e) {
            throw new StorageException("Could not start writing to file!", e);
        }
    }

    /**
     * Closes the BufferedWriter we are writing with
     * 
     * @throws StorageException
     *             When we cannot flush or close the BufferedWriter
     */
    public void stopWriting() throws StorageException {
        try {
            writer.flush();
            writer.close();
            writer = null;
        } catch (final IOException e) {
            throw new StorageException("Could not flush or close file!", e);
        }
    }

    /**
     * Writes the given JSONObject to the file
     * 
     * @param jO
     *            The JSONObject to write to the file
     * 
     * @throws StorageException
     *             When we fail to write the JSONObject
     */
    public void write(final JSONObject jO) throws StorageException {
        if (writer == null) {
            throw new IllegalStateException("Cannot write before initialising the BufferedWriter!");
        }
        try {
            writer.write(jO.toString());
            writer.newLine();
        } catch (final IOException e) {
            throw new StorageException("Could not write JSONObject to file (or could not add new line)!", e);
        }
    }

    /**
     * Gets ready to read from the file
     * 
     * @throws StorageException
     *             When we fail to create the BufferedReader
     */
    public void startReading() throws StorageException {
        if (reader != null) {
            stopReading();
        }
        try {
            if (!file.exists()) {
                create();
            }
            reader = new BufferedReader(new FileReader(file));
        } catch (final IOException e) {
            throw new StorageException("Could not start reading from file!", e);
        }
    }

    /**
     * Closes the BufferedReader we are reading with
     * 
     * @throws StorageException
     *             When we fail to close the BufferedReader
     */
    public void stopReading() throws StorageException {
        try {
            reader.close();
            reader = null;
        } catch (final IOException e) {
            throw new StorageException("Could not close BufferedReader!", e);
        }
    }

    /**
     * Reads a JSONObject from the file
     * 
     * @return The read and parsed JSONObject
     * 
     * @throws StorageException
     *             When we fail to read or parse the line
     */
    public JSONObject read() throws StorageException {
        try {
            final String line = reader.readLine();
            if (line != null && !line.equals("")) {
                return new JSONObject(line);
            } else {
                return null;
            }
        } catch (final JSONException e) {
            throw new StorageException("Could not create JSONObject from line!", e);
        } catch (final IOException e) {
            throw new StorageException("Could not read line from file!", e);
        }
    }

    /**
     * Backs up the file in a file with the same name + ".bck"
     * 
     * @throws StorageException
     *             When we fail to back up the file
     */
    public void backup() throws StorageException {
        try {
            final File bck = new File(file.getAbsolutePath() + ".bck");
            bck.delete();
            FileUtils.copyFile(file, bck);
        } catch (final IOException e) {
            throw new StorageException("Could not back up file!", e);
        }
    }

    /** Deletes the file */
    public void delete() {
        file.delete();
    }

    /**
     * Ensure the file is created
     * 
     * @throws StorageException
     *             When we cannot create the file
     */
    public void create() throws StorageException {
        if (file.exists()) {
            return;
        }
        try {
            file.createNewFile();
        } catch (final IOException e) {
            throw new StorageException("Could not create file!", e);
        }
    }
}
