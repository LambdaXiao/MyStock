package com.eno.base.enotree;
import java.util.Vector;
/**
 * <p>Title: ENOTree 类</p>
 * <p>Description: 树形结构管理.</p>
 * <p>Copyright: Copyright ENO(c) 2004</p>
 * <p>Company: ENO</p>
 * @author cheng-dn
 * @version 1.1
 */

public class ENOTree
{
  public ENOTree()
  {
    m_vectNodes = new Vector();
    // add virtual root node at first.
    m_pPNode = new ENOTreeNode();
    m_vectNodes.addElement(m_pPNode);
  }

  public ENOTree(Object rootobj)
  {
    m_vectNodes = new Vector();
    // add virtual root node at first.
    m_pPNode = new ENOTreeNode();
    m_pPNode.obj = rootobj;
    m_vectNodes.addElement(m_pPNode);
  }
  
  public void init()
  {
    init(-1,-1);
  }
  
  public void init(int nIniVar)
  {
    init(nIniVar,-1);
  }
  
  public void init(int nVar1,int nVar2)
  {
  }
  
  public void setRootNode(Object obj)
  {
    ENOTreeNode node = (ENOTreeNode)m_vectNodes.elementAt(0);
    node.obj = obj;
  }

  public ENOTreeNode getRootNode()
  {
    return (ENOTreeNode)m_vectNodes.elementAt(0);
  }

  /**
   * 取nIndex位置取一个树形结点.
   */
  public ENOTreeNode getAt(int nIndex)
  {
    ENOTreeNode node = null;
    if (nIndex >= 0 && nIndex < m_vectNodes.size() - 1)
    {
      node = (ENOTreeNode)m_vectNodes.elementAt(nIndex+1);
    }
    return node;
  }

  /**
   * 插入一个树形对象.
   */
  public void InsertObject(int nPID, Object obj)
  {
    ENOTreeNode node = new ENOTreeNode();
    node.nUID = this.size();
    node.nPUID = nPID;
    node.obj = obj;
    node.bortherNode = null;
    node.childNode = null;
    m_vectNodes.addElement(node);
  }

  /**
   * 根据uid从中取一个结点.
   */
  public ENOTreeNode getTreeNode(int nUID)
  {
    int nIndex = 0;
    ENOTreeNode node;
    int nSize = m_vectNodes.size();
    while(nIndex < nSize)
    {
      node = (ENOTreeNode)m_vectNodes.elementAt(nIndex);
      if (node.nUID == nUID)
        return node;
      nIndex++;
    }
    return null;
  }

  /**
   * 根据uid及level从中取一个结点.
   */
  public ENOTreeNode getTreeNode(int nUID,int nLevel)
  {
    int nIndex = 0;
    ENOTreeNode node;
    int nSize = m_vectNodes.size();
    while(nIndex < nSize)
    {
      node = (ENOTreeNode)m_vectNodes.elementAt(nIndex);
      if (node.nUID == nUID && node.nLevel == nLevel)
        return node;
      nIndex++;
    }
    return null;
  }

  /**
   * 插入一个树形对象.
   */
  public void InsertObject(int nUID,int nPID, Object obj)
  {
    ENOTreeNode node = new ENOTreeNode();
    node.nUID = nUID;
    node.nPUID = nPID;
    node.obj = obj;
    node.bortherNode = null;
    node.childNode = null;
    m_vectNodes.addElement(node);
  }
  
  /**
   * 插入一个树形对象.
   */
  public void InsertObject(int nIndex ,int nUID,int nPID, Object obj)
  {
    ENOTreeNode node = new ENOTreeNode();
    node.nUID = nUID;
    node.nPUID = nPID;
    node.obj = obj;
    node.bortherNode = null;
    node.childNode = null;
    m_vectNodes.insertElementAt(node, nIndex);
  }
  /**
   * 取树中对象数.
   */
  public int size()
  {
    return m_vectNodes.size() - 1;
  }

  public int GetMaxUid()
  {
	  int nMax = -1;
	  int size = size();
	  ENOTreeNode node;
	  for(int i=0;i<size ; i++)
	  {
		  node = (ENOTreeNode)m_vectNodes.elementAt(i);
		  if (node != null)
	      {
			  if(node.nUID > nMax)
				  nMax = node.nUID;
	      }
	  }
	  return nMax;
  }
  /**
   * 根据节点ID查找节点位置
   * @param nUID
   * @return
   */
  public int IndexOf(int nUID)
  {
	  int size = size();
	  ENOTreeNode node;
	  for(int i=0;i<size ; i++)
	  {
		  node = (ENOTreeNode)m_vectNodes.elementAt(i);
		  if (node != null)
	      {
			  if(node.nUID == nUID)
				  return i;
	      }
	  }
	  return -1;
  }
  /**
   * 根据位置删除节点
   * @param aIndex
   */
  public void DeleteTreeNode(int aIndex)
  {
	  if(aIndex >=0 && aIndex < size() )
		  m_vectNodes.removeElementAt(aIndex);
  }
  /**
   * 根据id/pid生成二叉树.
   */
  public void BuildTree(ENOTreeNode parentNode)
  {
    //根据Pid,生成pChildNode及pBortherNode;
    ENOTreeNode pNode = parentNode;
    if (pNode == null)
      pNode = (ENOTreeNode)m_vectNodes.elementAt(0);

    int nPid = pNode.nUID;
    int nSize = m_vectNodes.size();
    ENOTreeNode node;
    ENOTreeNode prevNode = null;
    int nIndex = 1;
    while(nIndex < nSize)
    {
      node = (ENOTreeNode)m_vectNodes.elementAt(nIndex);
      if (node != null)
      {
        if (node.nPUID == nPid)
        {
          if (prevNode != null)
            prevNode.bortherNode = node;
          else
            pNode.childNode = node;
          prevNode = node;
          node.nLevel = pNode.nLevel + 1;
          BuildTree(node);
        }
      }
      nIndex++;
    }
  }

  /**
   * 从根开始遍历.
   */
  public void DFS_Visit(TreeVisitor visitor)
  {
    DFS_Visit(null,visitor,false);
  }
  /**
   * 从某一结点开始遍历.
   */
  public void DFS_Visit(ENOTreeNode node,TreeVisitor visitor)
  {
    DFS_Visit(node,visitor,true);    
  }

  /**
   * 深度优先遍历
   */
  private void DFS_Visit(ENOTreeNode node,TreeVisitor visitor,boolean fVisitRoot)
  {
    if (node == null)
    { //from root.
      node = (ENOTreeNode)m_vectNodes.elementAt(0);
    }
    if (fVisitRoot)
    {
      visitor.visit(node);    
    }
    if (node.childNode != null)
      Node_Visit(node.childNode,visitor);
  }

  /**
   * 深度优先遍历
   */
  private void Node_Visit(ENOTreeNode node,TreeVisitor visitor)
  {
    visitor.visit(node);
    if (node.childNode != null)
      Node_Visit(node.childNode,visitor);
    visitor.nodeEnd(node);
    if (node.bortherNode != null)
      Node_Visit(node.bortherNode,visitor);
  }

  /**
   * 根据pid取子结点,只取一级,不往下遍历.
   */
  public Vector getChildNodes(int nPid)
  {
    ENOTreeNode pNode = getTreeNode(nPid);
    return getChildNodes(pNode,false);
  }

  /**
   * 根据node取子结点,只取一级,不往下遍历.
   */
  public Vector getChildNodes(ENOTreeNode node)
  {
    return getChildNodes(node,false);
  }
  /**
   * 根据node取子结点,只取一级,不往下遍历.
   */
  public Vector getChildNodes(ENOTreeNode node,boolean fNoChildren)
  {
    Vector vectRtn = new Vector();
    ENOTreeNode nodeTemp;
    if (node != null)
    {
      nodeTemp = node.childNode;
      while(nodeTemp != null)
      {
        if (fNoChildren == false || nodeTemp.childNode == null)
          vectRtn.addElement(nodeTemp);
        nodeTemp = nodeTemp.bortherNode;
      }
    }
    return vectRtn;
  }

  /**
   * 根据node取兄弟结点,只取一级,不往下遍历.
   */
  public Vector getBortherNodes(int nUID,boolean fNoChildren)
  {
    Vector vectRtn = new Vector();
    ENOTreeNode node = getTreeNode(nUID);
    if (node != null)
    {
      node = getTreeNode(node.nPUID);
      if (node != null)
      {
        vectRtn = getChildNodes(node,fNoChildren);
      }
    }
    return vectRtn;
  }
  /**
   * 根据id取到一个node的子结点.
   */
  public Vector getParentNodes(int nUID)
  {
    Vector vectRtn = new Vector();
    ENOTreeNode node = getTreeNode(nUID);
    while (node != null && node.nUID != -1)
    {
      vectRtn.addElement(node);
      node = getTreeNode(node.nPUID);
    }
    if (vectRtn.size() == 0)
      vectRtn = null;
    return vectRtn;
  }
  protected Vector m_vectNodes;
  protected ENOTreeNode m_pPNode;
}