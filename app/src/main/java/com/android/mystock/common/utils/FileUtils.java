package com.android.mystock.common.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;


/**
 * 文件操作对象
 */
public class FileUtils {

    public static final long ONE_KB = 1024L;
    public static final long ONE_MB = 1048576L;
    private static final long FILE_COPY_BUFFER_SIZE = 31457280L;


    /**
     * 写入byte文件
     *
     * @param context
     */
    public static void writeByte(Context context, String filePath, byte[] cont) {
        OutputStream os = null;

        try {
            os = context.openFileOutput(filePath, Context.MODE_PRIVATE);
            // 打开一个文件输出流。名称为txtME，模式为不覆盖
            os.write(cont);// 把内容写入文件

            // Toast.makeText(context, "写入菜单成功！",Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            // Toast.makeText(context, "没有找到文件！",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // Toast.makeText(context, "写入菜单失败！",Toast.LENGTH_SHORT).show();
        } finally {
            try {
                // 关闭文件输出流
                os.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 读取byte文件
     *
     * @param context
     * @return byte[]
     */
    public static byte[] readByte(Context context, String filePath) {
        byte data[] = null;
        FileInputStream is = null;

        try {

            is = context.openFileInput(filePath);
            ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
            // Data = new byte[102400];
            byte[] temp = new byte[1024];
            int size = 0;
            while ((size = is.read(temp)) != -1) {
                out.write(temp, 0, size);
            }
            data = out.toByteArray();

            // is.read(Data);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                // 关闭文件输入流
                if (is != null)
                    is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
        }
        return data;
    }

    /*
    SD卡是否有用
     */
    public static boolean isSDCardAvailable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除特定的文件;
     *
     * @param filePath
     * @return
     */
    public static boolean deleteSpicificFile(String filePath) {
        if (!isEmpty(filePath) && isSDCardAvailable()) {
            try {
                File file = new File(filePath);
                if (file.exists() && file.isFile()) {
                    return file.delete();
                }
            } catch (Exception e) {

                if (e != null && isNotEmpty(e.getMessage())) {
                    Log.e("@@@", e.getMessage());
                }
            }
        }
        return false;
    }

    public static boolean isEmpty(String text) {
        if (TextUtils.isEmpty(text)) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String text) {
        if (!TextUtils.isEmpty(text)) {
            return true;
        }
        return false;
    }

    /**
     * 根据字节数进行单位转换；
     *
     * @param size
     * @return
     */
    public static String getDataWithUnit(long size) {
        long kb = 1024;
        long mb = kb * 1024;

        if (size >= mb) {
            float f = (float) size / mb;
            return String.format("%.2f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format("%.2f KB", f);
        }

        return "0KB";
    }

    /**
     * 删除文件
     *
     * @param file
     * @return
     */
    public static boolean deleteFile(File file) {
        if (!file.exists()) {
            return false;
        }
        if (file.isFile()) {
            file.delete();
            return true;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if ((childFile == null) || (childFile.length == 0)) {
                file.delete();
                return true;
            }
            for (File f : childFile) {
                deleteFile(f);
            }
            file.delete();
        }
        return true;
    }


    /**
     * 文件是否存在
     *
     * @param file
     * @return
     */
    public static boolean isFileExists(String file) {
        try {
            File f = new File(file);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    /**
     * 目录是否为空
     *
     * @param directoryPath
     * @return
     */
    public static boolean isDirectoryEmpty(String directoryPath) {
        boolean isEmpty = true;
        File dirFile = new File(directoryPath);
        if (!dirFile.exists()) {
            return isEmpty;
        }
        if (dirFile.isDirectory()) {
            String[] files = dirFile.list();
            if ((files != null) && (files.length > 0)) {
                return false;
            }
        }
        return isEmpty;
    }

    public static String getFileExtension(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < filename.length() - 1)) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    public static String getFileNameByUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        int index = url.lastIndexOf('?');
        int index2 = url.lastIndexOf("/");
        if ((index > 0) && (index2 >= index)) {
            return UUID.randomUUID().toString();
        }
        return url.substring(index2 + 1, index < 0 ? url.length() : index);
    }

    public static void unZipAssets(Context context, String assetName, String outputDirectory)
            throws IOException {
        File file = new File(outputDirectory);
        if (!file.exists()) {
            file.mkdirs();
        }
        InputStream inputStream = context.getAssets().open(assetName);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        byte[] buffer = new byte[4096];
        int count = 0;
        while (zipEntry != null) {
            if (zipEntry.isDirectory()) {
                file = new File(outputDirectory + File.separator + zipEntry.getName());
                file.mkdir();
            } else {
                file = new File(outputDirectory + File.separator +
                        zipEntry.getName());
                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                while ((count = zipInputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, count);
                }
                fileOutputStream.close();
            }
            zipEntry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
    }

    public static int upZipFile(File zipFile, String folderPath)
            throws ZipException, IOException {
        ZipFile zfile = new ZipFile(zipFile);
        Enumeration zList = zfile.entries();
        ZipEntry ze = null;
        byte[] buf = new byte[1024];
        if (!folderPath.endsWith("/")) {
            Log.e("FileUtils", "folderPath is'st end with /!!!");

            folderPath = folderPath + File.separator;
        }
        Log.e("FileUtils", "folderPath = " + folderPath);
        while (zList.hasMoreElements()) {
            ze = (ZipEntry) zList.nextElement();
            if (ze.isDirectory()) {
                String dirstr = folderPath + ze.getName();
                dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
                File f = new File(dirstr);
                f.mkdirs();
            } else {
                OutputStream os = new BufferedOutputStream(new FileOutputStream(getRealFileName(folderPath, ze.getName())));
                InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
                int readLen = 0;
                while ((readLen = is.read(buf, 0, 1024)) != -1) {
                    os.write(buf, 0, readLen);
                }
                is.close();
                os.close();
            }
        }
        zfile.close();
        Log.d("FileUtils", "upZipFile finished!!!");
        return 0;
    }

    public static boolean copyFile(String sourceFilePath, String destFilePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(sourceFilePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return writeFile(destFilePath, inputStream);
    }

    public static boolean writeFile(String filePath, InputStream stream) {
        return writeFile(filePath, stream, false);
    }

    public static boolean writeFile(String filePath, InputStream stream, boolean append) {
        return writeFile(filePath != null ? new File(filePath) : null, stream, append);
    }

    public static boolean writeFile(File file, InputStream stream, boolean append) {
        OutputStream o = null;
        try {
            makeDirs(file.getAbsolutePath());
            o = new FileOutputStream(file, append);
            byte[] data = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
            }
            o.flush();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (o != null)
                try {
                    o.close();
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

    }

    public static boolean makeDirs(String filePath) {
        String folderName = getFolderName(filePath);
        if (TextUtils.isEmpty(folderName)) {
            return false;
        }

        File folder = new File(folderName);
        return (folder.exists()) && (folder.isDirectory()) ? true : folder.mkdirs();
    }

    public static String getFolderName(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return filePosi == -1 ? "" : filePath.substring(0, filePosi);
    }

    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return filePosi == -1 ? filePath : filePath.substring(filePosi + 1);
    }

    public static long getFileSize(String path) {
        if (TextUtils.isEmpty(path)) {
            return -1L;
        }

        File file = new File(path);
        return (file.exists()) && (file.isFile()) ? file.length() : -1L;
    }

    public static void copyAssetsFolder(Context context, String assetFolder, String destFolder)
            throws IOException {
        File fDestFolder = new File(destFolder);
        createDir(fDestFolder);
        AssetManager assetManager = context.getAssets();
        String[] files = assetManager.list(assetFolder);

        if (files == null) {
            return;
        }
        for (int i = 0; i < files.length; i++) {
            String absAssetFilePath = addTrailingSlash(assetFolder) + files[i];
            String[] subFiles = assetManager.list(absAssetFilePath);
            if (subFiles.length == 0) {
                String destFilePath = addTrailingSlash(destFolder) + files[i];
                copyAssetFile(context, absAssetFilePath, destFilePath);
            } else {
                copyAssetsFolder(context, absAssetFilePath, addTrailingSlash(destFolder) + files[i]);
            }
        }
    }

    public static void copyAssetFile(Context context, String assetFilePath, String destFilePath)
            throws IOException {
        InputStream in = context.getAssets().open(assetFilePath);
        OutputStream out = new FileOutputStream(destFilePath);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    private static void createDir(File dir)
            throws IOException {
        if (dir == null) {
            throw new IOException("dir is null");
        }
        if (dir.exists()) {
            if (!dir.isDirectory())
                throw new IOException("Can't create directory, a file is in the way");
        } else {
            dir.mkdirs();
            if (!dir.isDirectory())
                throw new IOException("Unable to create directory");
        }
    }

    private static String addTrailingSlash(String path) {
        if (path.charAt(path.length() - 1) != '/') {
            path = path + "/";
        }
        return path;
    }

    private static File getRealFileName(String baseDir, String absFileName) {
        String[] dirs = absFileName.split("/");
        File ret = new File(baseDir);
        String substr = null;
        if (dirs.length > 1) {
            for (int i = 0; i < dirs.length - 1; i++) {
                substr = dirs[i];
                try {
                    substr = new String(substr.getBytes("8859_1"), "GB2312");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                ret = new File(ret, substr);
            }

            if (!ret.exists())
                ret.mkdirs();
            substr = dirs[(dirs.length - 1)];
            try {
                substr = new String(substr.getBytes("8859_1"), "GB2312");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            ret = new File(ret, substr);
            return ret;
        }
        return ret;
    }

    public static void copyDirectoryToDirectory(File srcDir, File destDir)
            throws IOException {
        if (srcDir == null) {
            throw new NullPointerException("Source must not be null");
        }
        if ((srcDir.exists()) && (!srcDir.isDirectory())) {
            throw new IllegalArgumentException("Source '" + destDir + "' is not a directory");
        }
        if (destDir == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if ((destDir.exists()) && (!destDir.isDirectory())) {
            throw new IllegalArgumentException("Destination '" + destDir + "' is not a directory");
        }
        copyDirectory(srcDir, new File(destDir, srcDir.getName()), true);
    }

    public static void copyDirectory(File srcDir, File destDir, boolean preserveFileDate) throws IOException {
        copyDirectory(srcDir, destDir, null, preserveFileDate);
    }

    public static void copyDirectory(File srcDir, File destDir, FileFilter filter, boolean preserveFileDate) throws IOException {
        if (srcDir == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (destDir == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (!srcDir.exists()) {
            throw new FileNotFoundException("Source '" + srcDir + "' does not exist");
        }
        if (!srcDir.isDirectory()) {
            throw new IOException("Source '" + srcDir + "' exists but is not a directory");
        }
        if (srcDir.getCanonicalPath().equals(destDir.getCanonicalPath())) {
            throw new IOException("Source '" + srcDir + "' and destination '" + destDir + "' are the same");
        }

        List exclusionList = null;
        if (destDir.getCanonicalPath().startsWith(srcDir.getCanonicalPath())) {
            File[] srcFiles = filter == null ? srcDir.listFiles() : srcDir.listFiles(filter);
            if ((srcFiles != null) && (srcFiles.length > 0)) {
                exclusionList = new ArrayList(srcFiles.length);
                for (File srcFile : srcFiles) {
                    File copiedFile = new File(destDir, srcFile.getName());
                    exclusionList.add(copiedFile.getCanonicalPath());
                }
            }
        }
        doCopyDirectory(srcDir, destDir, filter, preserveFileDate, exclusionList);
    }

    private static void doCopyDirectory(File srcDir, File destDir, FileFilter filter, boolean preserveFileDate, List<String> exclusionList)
            throws IOException {
        File[] srcFiles = filter == null ? srcDir.listFiles() : srcDir.listFiles(filter);
        if (srcFiles == null) {
            throw new IOException("Failed to list contents of " + srcDir);
        }
        if (destDir.exists()) {
            if (!destDir.isDirectory()) {
                throw new IOException("Destination '" + destDir + "' exists but is not a directory");
            }
        } else if ((!destDir.mkdirs()) && (!destDir.isDirectory())) {
            throw new IOException("Destination '" + destDir + "' directory cannot be created");
        }

        if (!destDir.canWrite()) {
            throw new IOException("Destination '" + destDir + "' cannot be written to");
        }
        for (File srcFile : srcFiles) {
            File dstFile = new File(destDir, srcFile.getName());
            if ((exclusionList == null) || (!exclusionList.contains(srcFile.getCanonicalPath()))) {
                if (srcFile.isDirectory())
                    doCopyDirectory(srcFile, dstFile, filter, preserveFileDate, exclusionList);
                else {
                    doCopyFile(srcFile, dstFile, preserveFileDate);
                }
            }

        }

        if (preserveFileDate)
            destDir.setLastModified(srcDir.lastModified());
    }

    private static void doCopyFile(File srcFile, File destFile, boolean preserveFileDate)
            throws IOException {
        if ((destFile.exists()) && (destFile.isDirectory())) {
            throw new IOException("Destination '" + destFile + "' exists but is a directory");
        }

        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel input = null;
        FileChannel output = null;
        try {
            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(destFile);
            input = fis.getChannel();
            output = fos.getChannel();
            long size = input.size();
            long pos = 0L;
            long count = 0L;
            while (pos < size) {
                count = size - pos > 31457280L ? 31457280L : size - pos;
                pos += output.transferFrom(input, pos, count);
            }
        } finally {
            closeQuietly(output);
            closeQuietly(fos);
            closeQuietly(input);
            closeQuietly(fis);
        }

        if (srcFile.length() != destFile.length()) {
            throw new IOException("Failed to copy full contents from '" +
                    srcFile + "' to '" + destFile + "'");
        }
        if (preserveFileDate)
            destFile.setLastModified(srcFile.lastModified());
    }

    private static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null)
                closeable.close();
        } catch (IOException localIOException) {
        }
    }
}