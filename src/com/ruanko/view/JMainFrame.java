package com.ruanko.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import com.ruanko.listener.CommaryActionListener;
import com.ruanko.listener.ExitActionListener;
import com.ruanko.listener.ExportActionListener;
import com.ruanko.listener.ReadActionListener;
import com.ruanko.model.Channel;
import com.ruanko.model.News;
import com.ruanko.renderer.MyDefaultTreeCellRenderer;
import com.ruanko.service.RSSService;
import com.ruanko.service.UpdateThread;
import com.ruanko.util.NewsUtil;

public class JMainFrame extends JFrame {

	/**
	 * ID
	 */
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 800; // 窗口宽度
	private static final int HEIGHT = 600; // 窗口高度
	private static final String TITLE = "RSS阅读器"; // 窗口标题

	private JScrollPane newsScrollPane; // 包裹新闻内容的面板
	private JTextArea newsTextArea; // 新闻的详细信息显示区域
	private RSSService rssService; // rss的处理类
	private DefaultTableModel dtmTableModel; // 表格数据模型
	private JTable jtTable; // 表格
	private List<News> newsList; // 新闻列表
	private JButton exportBtn; // 导出按钮
	private JLabel status; // 状态栏标签
	private List<Channel> channelList;	//频道列表
	private JScrollPane treePane;

	public static JMainFrame mainFrame = null;

	public static JMainFrame getMainFrame() {
		return mainFrame;
	}

	public JMainFrame() {
		mainFrame = this;
		rssService = new RSSService();
		//获取新闻频道列表
		channelList=rssService.getChannelList();
		//开启后台线程下载最新新闻文件
		new UpdateThread(channelList).start();
		// 设置窗口的宽高
		setSize(WIDTH, HEIGHT);
		// 设置标题
		setTitle(TITLE);
		// 设置显示位置
		setLocationRelativeTo(null);
		// 设置关闭操作为关闭窗口
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// 设置主面板
		setContentPane(getMainJpanel());
		// 设置菜单栏
		setJMenuBar(getMyMenuBar());
		// 设置可见
		setVisible(true);
		// 第一次进入时显示第一频道的新闻,如果该新闻不存在则显示第二频道的新闻，以此类推
		firstEntryShow();
	}

	/**
	 * 
	 * @return 返回主面板
	 */
	public JPanel getMainJpanel() {
		JPanel mainJpanel = new JPanel();
		// 为主面板设置边界布局
		mainJpanel.setLayout(new BorderLayout());

		// 在主面板的南部放置一个状态栏
		mainJpanel.add(getStatusBar(), BorderLayout.SOUTH);
		// 在主面板的中部放置一个分隔面板的客户文本区
		mainJpanel.add(getJSPClientArea(), BorderLayout.CENTER);
		// 在主面板的北部放置一个工具栏
		mainJpanel.add(getToolBar(), BorderLayout.NORTH);
		//在主面板的西部放置树形控件
		mainJpanel.add(getTreePane(),BorderLayout.WEST);

		return mainJpanel;
	}

	/**
	 * 
	 * @return 包裹新闻内容的可卷的面板
	 */
	private JScrollPane getNewsScrollPane() {
		if (newsTextArea == null) {
			newsTextArea = new JTextArea();
			//自动换行
			newsTextArea.setLineWrap(true);
			//不可编辑
			newsTextArea.setEditable(false);
			//添加滚动条
			newsScrollPane = new JScrollPane(newsTextArea);
			// 设置尺寸
			newsScrollPane.setPreferredSize(new Dimension(780, 260));
			//设置滚动条为垂直
			newsScrollPane.setVerticalScrollBarPolicy( 
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		}
		return newsScrollPane;
	}

	/**
	 * 
	 * @return 返回表格
	 */
	private JScrollPane getJSPTable() {
		JScrollPane jspTable = null;
		if (jtTable == null) {
			// 创建表格数据模型，并且添加各列标题
			dtmTableModel = new DefaultTableModel() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				// 让表格的列表项不能被编辑
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			dtmTableModel.addColumn("主题");
			dtmTableModel.addColumn("接收时间");
			dtmTableModel.addColumn("发布时间");
			dtmTableModel.addColumn("作者");
			jtTable = new JTable(dtmTableModel);
			// 为表格列表项添加鼠标点击事件
			jtTable.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					// 判断是否是鼠标单击事件
					if (e.getClickCount() == 1) {
						int selectedRow = jtTable.getSelectedRow();
						News selectedNews = newsList.get(selectedRow);
						newsTextArea.setText(rssService
								.newsToString(selectedNews));
						//让光标在开头，滚动条不会在底部
						newsTextArea.setCaretPosition(0);
					}
				}
			});
			jspTable = new JScrollPane(jtTable);
		}
		return jspTable;
	}

	/**
	 * 
	 * @return 返回一个带图标的导出按钮
	 */
	private JButton getExportBtn() {
		if (exportBtn == null) {
			ImageIcon icon = new ImageIcon("images/export.jpg");
			icon.setImage(icon.getImage().getScaledInstance(32, 32,
					Image.SCALE_DEFAULT));
			// 创建导出按钮
			exportBtn = new JButton(icon);
			exportBtn.setToolTipText("导出");
			exportBtn.addActionListener(new ExportActionListener());
		}
		return exportBtn;
	}

	/**
	 * 
	 * @return 返回菜单栏
	 */
	private JMenuBar getMyMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		// 创建文件菜单，并设置快捷键
		JMenu fileMenu = new JMenu("文件(F)");
		// 快捷键 ALT + F
		fileMenu.setMnemonic(KeyEvent.VK_F);
		// 创建文件菜单下面的菜单项
		for (Channel channel : channelList) {
			JMenuItem item = new JMenuItem(channel.getName());
			item.addActionListener(new ReadActionListener(channel));
			fileMenu.add(item);
		}
		// 创建“导出”菜单项
		JMenuItem exportItem = new JMenuItem("导出");
		exportItem.addActionListener(new ExportActionListener());
		// 创建“退出”菜单项
		JMenuItem exitItem = new JMenuItem("退出");
		exitItem.addActionListener(new ExitActionListener());
		fileMenu.add(exportItem);
		fileMenu.add(exitItem);
		// 创建“帮助”菜单
		JMenu helpMenu = new JMenu("帮助(H)");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		// 创建“软件简介”菜单项
		JMenuItem showItem = new JMenuItem("软件说明");
		showItem.addActionListener(new CommaryActionListener());

		helpMenu.add(showItem);
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);

		return menuBar;
	}

	/**
	 * 
	 * @return 返回工具栏
	 */
	private JToolBar getToolBar() {
		// 创建工具栏
		JToolBar toolBar = new JToolBar();
		// 将导出按钮添加到工具栏中
		toolBar.add(getExportBtn());
		return toolBar;
	}

	/**
	 * 
	 * @return 返回分隔面板，上面是表格，下面是新闻具体内容
	 */
	private JSplitPane getJSPClientArea() {
		// 创建分隔面板，实现垂直分隔
		JSplitPane jspVertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		// 设置分隔条的位置
		jspVertical.setDividerLocation(280);
		// 将表格添加到分隔面板的上部
		jspVertical.setLeftComponent(getJSPTable());
		// 将新闻内容文本区添加到分隔面板的下部
		jspVertical.setRightComponent(getNewsScrollPane());
		return jspVertical;
	}

	/**
	 * 
	 * @return 返回状态栏面板
	 */
	private JPanel getStatusBar() {
		// 创建状态栏面板，并且设置相应的布局
		JPanel jsbMy = new JPanel();
		jsbMy.setLayout(new FlowLayout(FlowLayout.LEFT));
		// 创建标签，并且将标签添加到状态栏中
		status = new JLabel("www.ruanko.com");
		jsbMy.add(status);

		return jsbMy;
	}

	/**
	 * 刚进入界面时显示第一频道的新闻
	 */
	private void firstEntryShow() {
		Channel channel =channelList.get(0);
		String filePath = channel.getFilePath();
		newsList = rssService.getNewsList(filePath);
		if (newsList == null) {
			try {
				// 等待一秒钟让后台去下载新闻文件
				Thread.sleep(1000);
				// 再一次查看是否有了新闻内容
				newsList = rssService.getNewsList(filePath);
				// 如果有
				if (newsList != null) {
					status.setText(channel.getName());
					NewsUtil.showTable(newsList, dtmTableModel);
				} else {
					JOptionPane.showMessageDialog(null, "本地新闻文件不存在或新闻内容为空");
				}
			} catch (Exception e) {
			}
		} else {
			status.setText(channel.getName());
			NewsUtil.showTable(newsList, dtmTableModel);
		}
	}

	/**
	 * 读取新闻频道下的子新闻
	 * 
	 * @param channel
	 */
	public void readNews(Channel channel) {
		String filePath = channel.getFilePath();
		List<News> newsList = rssService.getNewsList(filePath);
		if (newsList != null) {
			this.newsList=newsList;
			status.setText(channel.getName());
			NewsUtil.showTable(newsList, dtmTableModel);
		} else {
			JOptionPane.showMessageDialog(null, "本地新闻xml文件不存在或格式错误");
		}
	}

	/**
	 * 导出新闻
	 */
	public void exportNews() {
		if (newsList != null) {
			if (rssService.saveNews(newsList)) {
				JOptionPane.showMessageDialog(null, "新闻信息保存成功");
			} else {
				JOptionPane.showMessageDialog(null, "新闻信息保存失败");
			}
		} else {
			JOptionPane.showMessageDialog(null, "请先选择新闻列表");
		}
	}
	
	/**
	 * 获得树形控件
	 * @return
	 */
	private JScrollPane getTreePane(){
		if(treePane==null){
			final JTree tree=new JTree();
			DefaultTreeModel defaultTreeModel = createModel();  
	          
	        //设置数据  
	        tree.setModel(defaultTreeModel);  
	          
	        //设置自定义描述类  
	        tree.setCellRenderer(new MyDefaultTreeCellRenderer());  
	        //为树形控件添加单击事件
	        tree.addTreeSelectionListener(new TreeSelectionListener() {
				@Override
				public void valueChanged(TreeSelectionEvent e) {
					DefaultMutableTreeNode treeNode=(DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
					String name=treeNode.toString();
					for(Channel channel:channelList){
						if(name.equals(channel.getName())){
							readNews(channel);
							break;
						}
					}
				}
			});
			treePane=new JScrollPane(tree);
		}
		return treePane;
	}
	
	  /** 
     * 创建树节点模型 
     * @return 
     */  
    public DefaultTreeModel createModel()  
    {  
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("新闻");  
        for(Channel channel:channelList){
        	DefaultMutableTreeNode node = new DefaultMutableTreeNode(channel.getName());  
        	root.add(node);
        }
        return new DefaultTreeModel(root);  
    }  
   
}
