package com.eno.base.enotree;
/**
 * <p>Title: TreeVisitor 类</p>
 * <p>Description: 树形遍历类,在遍历前被调用</p>
 * <p>Copyright: Copyright ENO(c) 2004</p>
 * <p>Company: ENO</p>
 * @author cheng-dn
 * @version 1.1
 */

public interface TreeVisitor
{
  /**
   * 当访问一个结点时被调用
   */
  public void visit(ENOTreeNode node);
  /**
   * 当一个结点结束时调用.
   */
  public void nodeEnd(ENOTreeNode node);
}