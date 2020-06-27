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
    val root = new File(path)
    val rootItem = new DirItem("", root.getName, 0, LocalDateTime.now, false, None)
    val rootPathLength = path.length
    val children = getDirChildren(root, rootPathLength)
    new DirTree(rootItem, children)
  }


  /**
    * Get given root dir children: files and subfolders recursively
    * @param root the root dir
    * @return sequence of DirTree objects: leaves (files) and branches (subdirectories)
    */
  def getDirChildren(root: File, rootPathLength: Int): Seq[DirTree] = {
    root.listFiles.map(_ match {
      case dir if dir.isDirectory => {
        val parent = getRelativePath(dir.getParent, rootPathLength)
        val node = new DirItem(parent, dir.getName, 0, LocalDateTime.now, false, None)
        val children = getDirChildren(dir, rootPathLength) // recursively traverse subdirectories
        new DirTree(node, children)
      }
      case file => {
        val fileLengthBytes = file.length
        val lastModifiedDate = localDateTimeFromMs(file.lastModified)
        val parent = getRelativePath(file.getParent, rootPathLength)
        val node = new DirItem(parent, file.getName, fileLengthBytes, lastModifiedDate, true, None)
        new DirTree(node, Seq())
      }
    }).toSeq
  }


  def getRelativePath(absolute: String, rootPathLength: Int): String = absolute match {
    case root if root.length <= rootPathLength => ""
    case notRoot => notRoot.substring(rootPathLength, absolute.length)
  }


  def localDateTimeFromMs(ms: Long) = LocalDateTime.ofInstant(Instant.ofEpochMilli(ms), ZoneId.systemDefault())
}