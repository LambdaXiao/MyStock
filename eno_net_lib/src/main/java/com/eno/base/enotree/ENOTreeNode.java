package com.eno.base.enotree;
/**
 * <p>Title: ENOTreeNode 类</p>
 * <p>Description: 树形结构中结点类</p>
 * <p>Copyright: Copyright ENO(c) 2004</p>
 * <p>Company: ENO</p>
 * @author cheng-dn
 * @version 1.1
 */

public class ENOTreeNode 
{
  public ENOTreeNode()
  {
		nUID = -1;
    nPUID = -1;
		obj = null;
    childNode = null;
		bortherNode = null;
    nLevel = 0;
  }
  /**
   * 在树中级数
   */
  public int nLevel;
  /**
   * 本身的唯一ID.
   */
  public int nUID;
  /**
   * 父ID
   */
	public int nPUID;
  /**
   * 结点中对象
   */
	public Object obj;
  /**
   * 子结点
   */
	public ENOTreeNode childNode;
  /**
   * 兄弟结点.
   */
	public ENOTreeNode bortherNode;
}