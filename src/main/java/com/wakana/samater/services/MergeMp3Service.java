package com.wakana.samater.services;

import java.io.BufferedOutputStream;
import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface  MergeMp3Service {
     List<String> modifyMediaUrls(List<String> mediaUrls,Long idLAngue);
     // CompletableFuture<File> mergeMP3FilesAsync(List<String> mp3FileUrls, String mergedFilePath) ;
     // public  void addSilence(BufferedOutputStream outputStream, int numFrames) ;

}
