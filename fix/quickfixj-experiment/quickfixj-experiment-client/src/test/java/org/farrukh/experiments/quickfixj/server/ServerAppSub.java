package org.farrukh.experiments.quickfixj.server;

import org.farrukh.experiments.quickfixj.shared.FixSettingsProvider;
import org.farrukh.experiments.quickfixj.shared.exception.FixException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quickfix.Acceptor;
import quickfix.ApplicationAdapter;
import quickfix.CompositeLogFactory;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.FieldNotFound;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.LogFactory;
import quickfix.Message;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.RuntimeError;
import quickfix.SLF4JLogFactory;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.SocketAcceptor;
import quickfix.UnsupportedMessageType;

public class ServerAppSub extends ApplicationAdapter {
    
    private static final Logger logger = LoggerFactory.getLogger(ServerAppSub.class);

    private static final String CONFIG_FILE = "server.cfg";

    private final Acceptor acceptor;
    
    private ServerStubMsgHandler msgHandler;
    
    public ServerAppSub() {
        try {
            SessionSettings settings = new FixSettingsProvider().loadSettings(CONFIG_FILE);
            MessageStoreFactory messageStoreFactory = new FileStoreFactory(settings);
            LogFactory logFactory = new CompositeLogFactory(new LogFactory[]{new SLF4JLogFactory(settings), new FileLogFactory(settings)});
            MessageFactory messageFactory = new DefaultMessageFactory();
            acceptor = new SocketAcceptor(this, messageStoreFactory, settings, logFactory, messageFactory);
            msgHandler = new ServerStubMsgHandler();
        } catch (ConfigError e) {
            throw new FixException(e);
        }
    }
    
    public void start() {
        try {
            acceptor.start();
            logger.info("The Server Stub is started");
        } catch (RuntimeError | ConfigError e) {
            throw new FixException(e);
        }
    }
    
    public void stop() {
        try {
            acceptor.stop();
            logger.info("The Server Stub is stoped");
        } catch (RuntimeError e) {
            throw new FixException(e);
        }
    }
    
    @Override
    public void fromApp(Message message, SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        msgHandler.crack(message, sessionId);
    }

}
