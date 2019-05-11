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
	private static final int WIDTH = 800; // ���ڿ��
	private static final int HEIGHT = 600; // ���ڸ߶�
	private static final String TITLE = "RSS�Ķ���"; // ���ڱ���

	private JScrollPane newsScrollPane; // �����������ݵ����
	private JTextArea newsTextArea; // ���ŵ���ϸ��Ϣ��ʾ����
	private RSSService rssService; // rss�Ĵ�����
	private DefaultTableModel dtmTableModel; // �������ģ��
	private JTable jtTable; // ���
	private List<News> newsList; // �����б�
	private JButton exportBtn; // ������ť
	private JLabel status; // ״̬����ǩ
	private List<Channel> channelList;	//Ƶ���б�
	private JScrollPane treePane;

	public static JMainFrame mainFrame = null;

	public static JMainFrame getMainFrame() {
		return mainFrame;
	}

	public JMainFrame() {
		mainFrame = this;
		rssService = new RSSService();
		//��ȡ����Ƶ���б�
		channelList=rssService.getChannelList();
		//������̨�߳��������������ļ�
		new UpdateThread(channelList).start();
		// ���ô��ڵĿ��
		setSize(WIDTH, HEIGHT);
		// ���ñ���
		setTitle(TITLE);
		// ������ʾλ��
		setLocationRelativeTo(null);
		// ���ùرղ���Ϊ�رմ���
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// ���������
		setContentPane(getMainJpanel());
		// ���ò˵���
		setJMenuBar(getMyMenuBar());
		// ���ÿɼ�
		setVisible(true);
		// ��һ�ν���ʱ��ʾ��һƵ��������,��������Ų���������ʾ�ڶ�Ƶ�������ţ��Դ�����
		firstEntryShow();
	}

	/**
	 * 
	 * @return ���������
	 */
	public JPanel getMainJpanel() {
		JPanel mainJpanel = new JPanel();
		// Ϊ��������ñ߽粼��
		mainJpanel.setLayout(new BorderLayout());

		// ���������ϲ�����һ��״̬��
		mainJpanel.add(getStatusBar(), BorderLayout.SOUTH);
		// ���������в�����һ���ָ����Ŀͻ��ı���
		mainJpanel.add(getJSPClientArea(), BorderLayout.CENTER);
		// �������ı�������һ��������
		mainJpanel.add(getToolBar(), BorderLayout.NORTH);
		//�������������������οؼ�
		mainJpanel.add(getTreePane(),BorderLayout.WEST);

		return mainJpanel;
	}

	/**
	 * 
	 * @return �����������ݵĿɾ�����
	 */
	private JScrollPane getNewsScrollPane() {
		if (newsTextArea == null) {
			newsTextArea = new JTextArea();
			//�Զ�����
			newsTextArea.setLineWrap(true);
			//���ɱ༭
			newsTextArea.setEditable(false);
			//��ӹ�����
			newsScrollPane = new JScrollPane(newsTextArea);
			// ���óߴ�
			newsScrollPane.setPreferredSize(new Dimension(780, 260));
			//���ù�����Ϊ��ֱ
			newsScrollPane.setVerticalScrollBarPolicy( 
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		}
		return newsScrollPane;
	}

	/**
	 * 
	 * @return ���ر��
	 */
	private JScrollPane getJSPTable() {
		JScrollPane jspTable = null;
		if (jtTable == null) {
			// �����������ģ�ͣ�������Ӹ��б���
			dtmTableModel = new DefaultTableModel() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				// �ñ����б���ܱ��༭
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			dtmTableModel.addColumn("����");
			dtmTableModel.addColumn("����ʱ��");
			dtmTableModel.addColumn("����ʱ��");
			dtmTableModel.addColumn("����");
			jtTable = new JTable(dtmTableModel);
			// Ϊ����б������������¼�
			jtTable.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					// �ж��Ƿ�����굥���¼�
					if (e.getClickCount() == 1) {
						int selectedRow = jtTable.getSelectedRow();
						News selectedNews = newsList.get(selectedRow);
						newsTextArea.setText(rssService
								.newsToString(selectedNews));
						//�ù���ڿ�ͷ�������������ڵײ�
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
	 * @return ����һ����ͼ��ĵ�����ť
	 */
	private JButton getExportBtn() {
		if (exportBtn == null) {
			ImageIcon icon = new ImageIcon("images/export.jpg");
			icon.setImage(icon.getImage().getScaledInstance(32, 32,
					Image.SCALE_DEFAULT));
			// ����������ť
			exportBtn = new JButton(icon);
			exportBtn.setToolTipText("����");
			exportBtn.addActionListener(new ExportActionListener());
		}
		return exportBtn;
	}

	/**
	 * 
	 * @return ���ز˵���
	 */
	private JMenuBar getMyMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		// �����ļ��˵��������ÿ�ݼ�
		JMenu fileMenu = new JMenu("�ļ�(F)");
		// ��ݼ� ALT + F
		fileMenu.setMnemonic(KeyEvent.VK_F);
		// �����ļ��˵�����Ĳ˵���
		for (Channel channel : channelList) {
			JMenuItem item = new JMenuItem(channel.getName());
			item.addActionListener(new ReadActionListener(channel));
			fileMenu.add(item);
		}
		// �������������˵���
		JMenuItem exportItem = new JMenuItem("����");
		exportItem.addActionListener(new ExportActionListener());
		// �������˳����˵���
		JMenuItem exitItem = new JMenuItem("�˳�");
		exitItem.addActionListener(new ExitActionListener());
		fileMenu.add(exportItem);
		fileMenu.add(exitItem);
		// �������������˵�
		JMenu helpMenu = new JMenu("����(H)");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		// �����������顱�˵���
		JMenuItem showItem = new JMenuItem("���˵��");
		showItem.addActionListener(new CommaryActionListener());

		helpMenu.add(showItem);
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);

		return menuBar;
	}

	/**
	 * 
	 * @return ���ع�����
	 */
	private JToolBar getToolBar() {
		// ����������
		JToolBar toolBar = new JToolBar();
		// ��������ť��ӵ���������
		toolBar.add(getExportBtn());
		return toolBar;
	}

	/**
	 * 
	 * @return ���طָ���壬�����Ǳ�����������ž�������
	 */
	private JSplitPane getJSPClientArea() {
		// �����ָ���壬ʵ�ִ�ֱ�ָ�
		JSplitPane jspVertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		// ���÷ָ�����λ��
		jspVertical.setDividerLocation(280);
		// �������ӵ��ָ������ϲ�
		jspVertical.setLeftComponent(getJSPTable());
		// �����������ı�����ӵ��ָ������²�
		jspVertical.setRightComponent(getNewsScrollPane());
		return jspVertical;
	}

	/**
	 * 
	 * @return ����״̬�����
	 */
	private JPanel getStatusBar() {
		// ����״̬����壬����������Ӧ�Ĳ���
		JPanel jsbMy = new JPanel();
		jsbMy.setLayout(new FlowLayout(FlowLayout.LEFT));
		// ������ǩ�����ҽ���ǩ��ӵ�״̬����
		status = new JLabel("www.ruanko.com");
		jsbMy.add(status);

		return jsbMy;
	}

	/**
	 * �ս������ʱ��ʾ��һƵ��������
	 */
	private void firstEntryShow() {
		Channel channel =channelList.get(0);
		String filePath = channel.getFilePath();
		newsList = rssService.getNewsList(filePath);
		if (newsList == null) {
			try {
				// �ȴ�һ�����ú�̨ȥ���������ļ�
				Thread.sleep(1000);
				// ��һ�β鿴�Ƿ�������������
				newsList = rssService.getNewsList(filePath);
				// �����
				if (newsList != null) {
					status.setText(channel.getName());
					NewsUtil.showTable(newsList, dtmTableModel);
				} else {
					JOptionPane.showMessageDialog(null, "���������ļ������ڻ���������Ϊ��");
				}
			} catch (Exception e) {
			}
		} else {
			status.setText(channel.getName());
			NewsUtil.showTable(newsList, dtmTableModel);
		}
	}

	/**
	 * ��ȡ����Ƶ���µ�������
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
			JOptionPane.showMessageDialog(null, "��������xml�ļ������ڻ��ʽ����");
		}
	}

	/**
	 * ��������
	 */
	public void exportNews() {
		if (newsList != null) {
			if (rssService.saveNews(newsList)) {
				JOptionPane.showMessageDialog(null, "������Ϣ����ɹ�");
			} else {
				JOptionPane.showMessageDialog(null, "������Ϣ����ʧ��");
			}
		} else {
			JOptionPane.showMessageDialog(null, "����ѡ�������б�");
		}
	}
	
	/**
	 * ������οؼ�
	 * @return
	 */
	private JScrollPane getTreePane(){
		if(treePane==null){
			final JTree tree=new JTree();
			DefaultTreeModel defaultTreeModel = createModel();  
	          
	        //��������  
	        tree.setModel(defaultTreeModel);  
	          
	        //�����Զ���������  
	        tree.setCellRenderer(new MyDefaultTreeCellRenderer());  
	        //Ϊ���οؼ���ӵ����¼�
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
     * �������ڵ�ģ�� 
     * @return 
     */  
    public DefaultTreeModel createModel()  
    {  
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("����");  
        for(Channel channel:channelList){
        	DefaultMutableTreeNode node = new DefaultMutableTreeNode(channel.getName());  
        	root.add(node);
        }
        return new DefaultTreeModel(root);  
    }  
   
}
