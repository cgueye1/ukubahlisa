package com.wakana.samater.services;

public interface FileTrasfertService {
  void  deleteFileFromRemote(String remoteFilePath);
  void transferFileToRemote(String localFilePath, String remoteFilePath) ;
}
