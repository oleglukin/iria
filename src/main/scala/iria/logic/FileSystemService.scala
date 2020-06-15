package iria.logic

import iria.model.{DirItem, DirTree}
import java.io.File
import java.time.{LocalDateTime, Instant, ZoneId}

object FileSystemService {
  /**
    * Traverse files and subdirectories and construct DirTree
    * @param path directory path to start from
    * @return DirTree structure with files and subdirectories
    */
  def dirTreeFromPath(path: String): DirTree = {
    val root = new File(path) // TODO handle invalid root path
    val rootItem = new DirItem(path, root.getName, 0, LocalDateTime.now, false, None)
    val children = getDirChildren(root)
    new DirTree(rootItem, children)
  }


  /**
    * Get given root dir children: files and subfolders recursively
    * @param root the root dir
    * @return sequence of DirTree objects: leaves (files) and branches (subdirectories)
    */
  def getDirChildren(root: File): Seq[DirTree] = {
    root.listFiles.map(_ match {
      case dir if dir.isDirectory => {
        val node = new DirItem(dir.getParent, dir.getName, 0, LocalDateTime.now, false, None)
        val children = getDirChildren(dir) // recursively traverse subdirectories
        new DirTree(node, children)
      }
      case file => {
        val fileLengthBytes = file.length
        val lastModifiedDate = localDateTimeFromMs(file.lastModified)
        val node = new DirItem(file.getParent, file.getName, fileLengthBytes, lastModifiedDate, true, None)
        new DirTree(node, Seq())
      }
    }).toSeq
  }


  def localDateTimeFromMs(ms: Long) = LocalDateTime.ofInstant(Instant.ofEpochMilli(ms), ZoneId.systemDefault())
}