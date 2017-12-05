package com.device.manage.core.fastdfs;

import com.google.gson.Gson;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * File Manager used to provide the services to upload / download / delete the files
 * from FastDFS.
 * <p>
 * <note>In this version, FileManager only support single tracker, will enhance this later...</note>
 *
 */
public class FileManager implements FileManagerConfig {

    private static final long serialVersionUID = 1L;

    private static Logger logger = LoggerFactory.getLogger(FileManager.class);

    private static TrackerClient trackerClient;
    private static TrackerServer trackerServer;
    private static StorageServer storageServer;
    private static StorageClient storageClient;

    private static final Gson gson = new Gson();

    static { // Initialize Fast DFS Client configurations

        try {
            String classPath = FileManager.class.getResource("/").getPath();
            String fdfsClientConfigFilePath = classPath + SEPARATOR + CLIENT_CONFIG_FILE;
            logger.info("Fast DFS configuration file path:" + fdfsClientConfigFilePath);
            ClientGlobal.init(fdfsClientConfigFilePath);

            trackerClient = new TrackerClient();
            trackerServer = trackerClient.getConnection();

            storageClient = new StorageClient(trackerServer, storageServer);

        } catch (Exception e) {
            logger.error(e.getMessage());

        }
    }


    public static String upload(FastDFSFile file) {
        logger.info("File Name: " + file.getName() + "		File Length: " + file.getContent().length);
        String fileAbsolutePath = null;
        NameValuePair[] meta_list = new NameValuePair[3];
        meta_list[0] = new NameValuePair("width", FILE_DEFAULT_WIDTH);
        meta_list[1] = new NameValuePair("height", FILE_DEFAULT_HEIGHT);
        meta_list[2] = new NameValuePair("author", FILE_DEFAULT_AUTHOR);

        long startTime = System.currentTimeMillis();
        String[] uploadResults = null;
        try {
            uploadResults = storageClient.upload_file(file.getContent(), file.getExt(), meta_list);
        } catch (IOException e) {
            logger.error("IO Exception when upload the file: " + file.getName(), e);
        } catch (Exception e) {
            logger.error("Non IO Exception when upload the file: " + file.getName(), e);
        }
        logger.info("upload_file time used: " + (System.currentTimeMillis() - startTime) + " ms");

        if (uploadResults == null) {
            logger.error("upload file fail, error code: " + storageClient.getErrorCode());
        } else {

            String groupName = uploadResults[0];
            String remoteFileName = uploadResults[1];

            fileAbsolutePath =  SEPARATOR + groupName + SEPARATOR + remoteFileName;

            logger.info("upload file successfully!!!  " + "group_name: " + groupName + ", remoteFileName:"
                    + " " + remoteFileName);

        }
        return fileAbsolutePath;

    }

    public static FileInfo getFile(String groupName, String remoteFileName) {
        try {
            return storageClient.get_file_info(groupName, remoteFileName);
        } catch (IOException e) {
            logger.error("IO Exception: Get File from Fast DFS failed", e);
        } catch (Exception e) {
            logger.error("Non IO Exception: Get File from Fast DFS failed", e);
        }
        return null;
    }

    public static boolean downloadFile(String remoteFileName, String groupName, String fileSavePath) throws IOException, MyException {

        byte[] bytes = storageClient.download_file(groupName, remoteFileName);
        try {
            File outFile = new File(fileSavePath);
            if (!outFile.exists()) {
                outFile.createNewFile();
            } else {
                System.out.println("cunzai ");
            }
            FileOutputStream fileOutputStream = new FileOutputStream(outFile);
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static void deleteFile(String groupName, String remoteFileName) throws Exception {
        storageClient.delete_file(groupName, remoteFileName);
    }

    public static StorageServer[] getStoreStorages(String groupName) throws IOException {
        return trackerClient.getStoreStorages(trackerServer, groupName);
    }

    public static ServerInfo[] getFetchStorages(String groupName, String remoteFileName) throws IOException {
        return trackerClient.getFetchStorages(trackerServer, groupName, remoteFileName);
    }

    public static int test() {
        System.out.println(FileManager.class.getClassLoader().getResource("/").getPath());
        return 1;
    }
}