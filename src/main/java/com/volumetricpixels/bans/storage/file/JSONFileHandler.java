package com.volumetricpixels.bans.storage.file;

import java.io.File;
import java.io.IOException;

import lib.org.json.JSONException;
import lib.org.json.JSONObject;

import com.volumetricpixels.bans.exception.StorageException;

/**
 * A utility for storing JSON data in a File
 */
public final class JSONFileHandler extends FileHandler {
    /**
     * Creates a new JSONFileHandler with the given File
     * 
     * @param file
     *            The File to write to / read from
     */
    public JSONFileHandler(final File file) {
        super(file);
    }

    /**
     * Writes the given JSONObject to the file
     * 
     * @param jO
     *            The JSONObject to write to the file
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
     * Reads a JSONObject from the file
     * 
     * @return The read and parsed JSONObject
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
}
