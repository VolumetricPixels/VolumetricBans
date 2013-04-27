package com.volumetricpixels.bans.storage;

import java.io.*;

import org.apache.commons.io.FileUtils;

import com.volumetricpixels.bans.exception.StorageException;

public abstract class FileHandler {
    /** The File we are writing to and/or reading from */
    protected final File file;

    /** The BufferedWriter in use */
    protected BufferedWriter writer;
    /** The BufferedReader in use */
    protected BufferedReader reader;
    /** The current backup path */
    protected String backupPath;

    protected FileHandler(final File file) {
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
     * Backs up the file in a file with the same name + ".bck"
     * 
     * @throws StorageException
     *             When we fail to back up the file
     */
    public void backup() throws StorageException {
        final File bck = new File(backupPath = file.getAbsolutePath() + ".bck");
        try {
            bck.delete();
            bck.createNewFile();
            FileUtils.copyFile(file, bck);
        } catch (final IOException e) {
            throw new StorageException("Could not back up file!", e);
        }
    }

    /**
     * Restores a backup of the file that was saved using <code>backup()</code>
     * 
     * @throws StorageException
     *             If the file cannot be backed up
     */
    public void restoreBackup() throws StorageException {
        final File bck = new File(backupPath);
        try {
            delete();
            create();
            FileUtils.copyFile(bck, file);
        } catch (final Exception e) {
            throw new StorageException(e);
        }
    }

    /**
     * Deletes the file if said file exists
     * 
     * @throws IllegalStateException
     *             If the file is null or does not exist
     * @throws IOException
     *             If the file cannot be deleted
     */
    public void delete() throws IllegalStateException, IOException {
        if (file == null || !file.exists()) {
            throw new IllegalStateException("File is null or does not exist!");
        } else if (!file.delete()) {
            throw new IOException("Could not delete file at: " + file.getAbsolutePath());
        }
    }

    /**
     * Deletes the file on exit if said file exists
     * 
     * @throws IllegalStateException
     *             If the file is null or does not exist
     */
    public void deleteOnExit() throws IllegalStateException {
        if (file == null || !file.exists()) {
            throw new IllegalStateException("File is null or does not exist!");
        }
        file.deleteOnExit();
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
