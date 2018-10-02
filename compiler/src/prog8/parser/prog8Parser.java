// Generated from /home/irmen/Projects/prog8/compiler/antlr/prog8.g4 by ANTLR 4.7
package prog8.parser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class prog8Parser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, T__32=33, T__33=34, T__34=35, T__35=36, T__36=37, T__37=38, 
		T__38=39, T__39=40, T__40=41, T__41=42, T__42=43, T__43=44, T__44=45, 
		T__45=46, T__46=47, T__47=48, T__48=49, T__49=50, T__50=51, T__51=52, 
		T__52=53, T__53=54, T__54=55, T__55=56, T__56=57, T__57=58, T__58=59, 
		T__59=60, T__60=61, T__61=62, T__62=63, T__63=64, T__64=65, T__65=66, 
		T__66=67, T__67=68, T__68=69, T__69=70, T__70=71, T__71=72, T__72=73, 
		T__73=74, T__74=75, T__75=76, T__76=77, T__77=78, T__78=79, T__79=80, 
		T__80=81, T__81=82, T__82=83, T__83=84, T__84=85, T__85=86, T__86=87, 
		T__87=88, T__88=89, T__89=90, T__90=91, T__91=92, T__92=93, T__93=94, 
		T__94=95, T__95=96, T__96=97, T__97=98, T__98=99, T__99=100, T__100=101, 
		T__101=102, T__102=103, T__103=104, LINECOMMENT=105, COMMENT=106, WS=107, 
		EOL=108, NAME=109, DEC_INTEGER=110, HEX_INTEGER=111, BIN_INTEGER=112, 
		FLOAT_NUMBER=113, STRING=114, INLINEASMBLOCK=115, SINGLECHAR=116;
	public static final int
		RULE_module = 0, RULE_modulestatement = 1, RULE_block = 2, RULE_statement = 3, 
		RULE_labeldef = 4, RULE_unconditionaljump = 5, RULE_directive = 6, RULE_directivearg = 7, 
		RULE_vardecl = 8, RULE_varinitializer = 9, RULE_constdecl = 10, RULE_memoryvardecl = 11, 
		RULE_datatype = 12, RULE_arrayspec = 13, RULE_assignment = 14, RULE_augassignment = 15, 
		RULE_assign_target = 16, RULE_postincrdecr = 17, RULE_expression = 18, 
		RULE_arrayindexed = 19, RULE_functioncall = 20, RULE_functioncall_stmt = 21, 
		RULE_expression_list = 22, RULE_returnstmt = 23, RULE_breakstmt = 24, 
		RULE_continuestmt = 25, RULE_identifier = 26, RULE_scoped_identifier = 27, 
		RULE_register = 28, RULE_statusregister = 29, RULE_integerliteral = 30, 
		RULE_wordsuffix = 31, RULE_booleanliteral = 32, RULE_arrayliteral = 33, 
		RULE_stringliteral = 34, RULE_charliteral = 35, RULE_floatliteral = 36, 
		RULE_literalvalue = 37, RULE_inlineasm = 38, RULE_subroutine = 39, RULE_sub_return_part = 40, 
		RULE_statement_block = 41, RULE_sub_params = 42, RULE_sub_param = 43, 
		RULE_sub_returns = 44, RULE_asmsubroutine = 45, RULE_asmsub_address = 46, 
		RULE_asmsub_params = 47, RULE_asmsub_param = 48, RULE_clobber = 49, RULE_asmsub_returns = 50, 
		RULE_asmsub_return = 51, RULE_if_stmt = 52, RULE_else_part = 53, RULE_branch_stmt = 54, 
		RULE_branchcondition = 55, RULE_forloop = 56, RULE_whileloop = 57, RULE_repeatloop = 58;
	public static final String[] ruleNames = {
		"module", "modulestatement", "block", "statement", "labeldef", "unconditionaljump", 
		"directive", "directivearg", "vardecl", "varinitializer", "constdecl", 
		"memoryvardecl", "datatype", "arrayspec", "assignment", "augassignment", 
		"assign_target", "postincrdecr", "expression", "arrayindexed", "functioncall", 
		"functioncall_stmt", "expression_list", "returnstmt", "breakstmt", "continuestmt", 
		"identifier", "scoped_identifier", "register", "statusregister", "integerliteral", 
		"wordsuffix", "booleanliteral", "arrayliteral", "stringliteral", "charliteral", 
		"floatliteral", "literalvalue", "inlineasm", "subroutine", "sub_return_part", 
		"statement_block", "sub_params", "sub_param", "sub_returns", "asmsubroutine", 
		"asmsub_address", "asmsub_params", "asmsub_param", "clobber", "asmsub_returns", 
		"asmsub_return", "if_stmt", "else_part", "branch_stmt", "branchcondition", 
		"forloop", "whileloop", "repeatloop"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'~'", "':'", "'goto'", "'%output'", "'%launcher'", "'%zeropage'", 
		"'%address'", "'%import'", "'%breakpoint'", "'%asminclude'", "'%asmbinary'", 
		"'%option'", "','", "'='", "'const'", "'memory'", "'byte'", "'word'", 
		"'float'", "'str'", "'str_p'", "'str_s'", "'str_ps'", "'['", "']'", "'+='", 
		"'-='", "'/='", "'//='", "'*='", "'**='", "'&='", "'|='", "'^='", "'++'", 
		"'--'", "'('", "')'", "'+'", "'-'", "'**'", "'*'", "'/'", "'//'", "'%'", 
		"'<'", "'>'", "'<='", "'>='", "'=='", "'!='", "'&'", "'^'", "'|'", "'to'", 
		"'step'", "'and'", "'or'", "'xor'", "'not'", "'return'", "'break'", "'continue'", 
		"'.'", "'A'", "'X'", "'Y'", "'AX'", "'AY'", "'XY'", "'Pc'", "'Pz'", "'Pn'", 
		"'Pv'", "'.w'", "'true'", "'false'", "'%asm'", "'sub'", "'->'", "'{'", 
		"'}'", "'asmsub'", "'clobbers'", "'@'", "'if'", "'else'", "'if_cs'", "'if_cc'", 
		"'if_eq'", "'if_z'", "'if_ne'", "'if_nz'", "'if_pl'", "'if_pos'", "'if_mi'", 
		"'if_neg'", "'if_vs'", "'if_vc'", "'for'", "'in'", "'while'", "'repeat'", 
		"'until'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, "LINECOMMENT", "COMMENT", 
		"WS", "EOL", "NAME", "DEC_INTEGER", "HEX_INTEGER", "BIN_INTEGER", "FLOAT_NUMBER", 
		"STRING", "INLINEASMBLOCK", "SINGLECHAR"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "prog8.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public prog8Parser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ModuleContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(prog8Parser.EOF, 0); }
		public List<ModulestatementContext> modulestatement() {
			return getRuleContexts(ModulestatementContext.class);
		}
		public ModulestatementContext modulestatement(int i) {
			return getRuleContext(ModulestatementContext.class,i);
		}
		public List<TerminalNode> EOL() { return getTokens(prog8Parser.EOL); }
		public TerminalNode EOL(int i) {
			return getToken(prog8Parser.EOL, i);
		}
		public ModuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_module; }
	}

	public final ModuleContext module() throws RecognitionException {
		ModuleContext _localctx = new ModuleContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_module);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(122);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11))) != 0) || _la==EOL) {
				{
				setState(120);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__0:
				case T__3:
				case T__4:
				case T__5:
				case T__6:
				case T__7:
				case T__8:
				case T__9:
				case T__10:
				case T__11:
					{
					setState(118);
					modulestatement();
					}
					break;
				case EOL:
					{
					setState(119);
					match(EOL);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(124);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(125);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ModulestatementContext extends ParserRuleContext {
		public DirectiveContext directive() {
			return getRuleContext(DirectiveContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ModulestatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modulestatement; }
	}

	public final ModulestatementContext modulestatement() throws RecognitionException {
		ModulestatementContext _localctx = new ModulestatementContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_modulestatement);
		try {
			setState(129);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__3:
			case T__4:
			case T__5:
			case T__6:
			case T__7:
			case T__8:
			case T__9:
			case T__10:
			case T__11:
				enterOuterAlt(_localctx, 1);
				{
				setState(127);
				directive();
				}
				break;
			case T__0:
				enterOuterAlt(_localctx, 2);
				{
				setState(128);
				block();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BlockContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public Statement_blockContext statement_block() {
			return getRuleContext(Statement_blockContext.class,0);
		}
		public TerminalNode EOL() { return getToken(prog8Parser.EOL, 0); }
		public IntegerliteralContext integerliteral() {
			return getRuleContext(IntegerliteralContext.class,0);
		}
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(131);
			match(T__0);
			setState(132);
			identifier();
			setState(134);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 110)) & ~0x3f) == 0 && ((1L << (_la - 110)) & ((1L << (DEC_INTEGER - 110)) | (1L << (HEX_INTEGER - 110)) | (1L << (BIN_INTEGER - 110)))) != 0)) {
				{
				setState(133);
				integerliteral();
				}
			}

			setState(136);
			statement_block();
			setState(137);
			match(EOL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatementContext extends ParserRuleContext {
		public DirectiveContext directive() {
			return getRuleContext(DirectiveContext.class,0);
		}
		public VarinitializerContext varinitializer() {
			return getRuleContext(VarinitializerContext.class,0);
		}
		public VardeclContext vardecl() {
			return getRuleContext(VardeclContext.class,0);
		}
		public ConstdeclContext constdecl() {
			return getRuleContext(ConstdeclContext.class,0);
		}
		public MemoryvardeclContext memoryvardecl() {
			return getRuleContext(MemoryvardeclContext.class,0);
		}
		public AssignmentContext assignment() {
			return getRuleContext(AssignmentContext.class,0);
		}
		public AugassignmentContext augassignment() {
			return getRuleContext(AugassignmentContext.class,0);
		}
		public UnconditionaljumpContext unconditionaljump() {
			return getRuleContext(UnconditionaljumpContext.class,0);
		}
		public PostincrdecrContext postincrdecr() {
			return getRuleContext(PostincrdecrContext.class,0);
		}
		public Functioncall_stmtContext functioncall_stmt() {
			return getRuleContext(Functioncall_stmtContext.class,0);
		}
		public If_stmtContext if_stmt() {
			return getRuleContext(If_stmtContext.class,0);
		}
		public Branch_stmtContext branch_stmt() {
			return getRuleContext(Branch_stmtContext.class,0);
		}
		public SubroutineContext subroutine() {
			return getRuleContext(SubroutineContext.class,0);
		}
		public AsmsubroutineContext asmsubroutine() {
			return getRuleContext(AsmsubroutineContext.class,0);
		}
		public InlineasmContext inlineasm() {
			return getRuleContext(InlineasmContext.class,0);
		}
		public ReturnstmtContext returnstmt() {
			return getRuleContext(ReturnstmtContext.class,0);
		}
		public ForloopContext forloop() {
			return getRuleContext(ForloopContext.class,0);
		}
		public WhileloopContext whileloop() {
			return getRuleContext(WhileloopContext.class,0);
		}
		public RepeatloopContext repeatloop() {
			return getRuleContext(RepeatloopContext.class,0);
		}
		public BreakstmtContext breakstmt() {
			return getRuleContext(BreakstmtContext.class,0);
		}
		public ContinuestmtContext continuestmt() {
			return getRuleContext(ContinuestmtContext.class,0);
		}
		public LabeldefContext labeldef() {
			return getRuleContext(LabeldefContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_statement);
		try {
			setState(161);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(139);
				directive();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(140);
				varinitializer();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(141);
				vardecl();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(142);
				constdecl();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(143);
				memoryvardecl();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(144);
				assignment();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(145);
				augassignment();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(146);
				unconditionaljump();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(147);
				postincrdecr();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(148);
				functioncall_stmt();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(149);
				if_stmt();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(150);
				branch_stmt();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(151);
				subroutine();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(152);
				asmsubroutine();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(153);
				inlineasm();
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(154);
				returnstmt();
				}
				break;
			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(155);
				forloop();
				}
				break;
			case 18:
				enterOuterAlt(_localctx, 18);
				{
				setState(156);
				whileloop();
				}
				break;
			case 19:
				enterOuterAlt(_localctx, 19);
				{
				setState(157);
				repeatloop();
				}
				break;
			case 20:
				enterOuterAlt(_localctx, 20);
				{
				setState(158);
				breakstmt();
				}
				break;
			case 21:
				enterOuterAlt(_localctx, 21);
				{
				setState(159);
				continuestmt();
				}
				break;
			case 22:
				enterOuterAlt(_localctx, 22);
				{
				setState(160);
				labeldef();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LabeldefContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public LabeldefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_labeldef; }
	}

	public final LabeldefContext labeldef() throws RecognitionException {
		LabeldefContext _localctx = new LabeldefContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_labeldef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(163);
			identifier();
			setState(164);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UnconditionaljumpContext extends ParserRuleContext {
		public IntegerliteralContext integerliteral() {
			return getRuleContext(IntegerliteralContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public Scoped_identifierContext scoped_identifier() {
			return getRuleContext(Scoped_identifierContext.class,0);
		}
		public UnconditionaljumpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unconditionaljump; }
	}

	public final UnconditionaljumpContext unconditionaljump() throws RecognitionException {
		UnconditionaljumpContext _localctx = new UnconditionaljumpContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_unconditionaljump);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(166);
			match(T__2);
			setState(170);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				{
				setState(167);
				integerliteral();
				}
				break;
			case 2:
				{
				setState(168);
				identifier();
				}
				break;
			case 3:
				{
				setState(169);
				scoped_identifier();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DirectiveContext extends ParserRuleContext {
		public Token directivename;
		public List<DirectiveargContext> directivearg() {
			return getRuleContexts(DirectiveargContext.class);
		}
		public DirectiveargContext directivearg(int i) {
			return getRuleContext(DirectiveargContext.class,i);
		}
		public DirectiveContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_directive; }
	}

	public final DirectiveContext directive() throws RecognitionException {
		DirectiveContext _localctx = new DirectiveContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_directive);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(172);
			((DirectiveContext)_localctx).directivename = _input.LT(1);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11))) != 0)) ) {
				((DirectiveContext)_localctx).directivename = (Token)_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(184);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				{
				setState(174);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
				case 1:
					{
					setState(173);
					directivearg();
					}
					break;
				}
				}
				break;
			case 2:
				{
				setState(176);
				directivearg();
				setState(181);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__12) {
					{
					{
					setState(177);
					match(T__12);
					setState(178);
					directivearg();
					}
					}
					setState(183);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DirectiveargContext extends ParserRuleContext {
		public StringliteralContext stringliteral() {
			return getRuleContext(StringliteralContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public IntegerliteralContext integerliteral() {
			return getRuleContext(IntegerliteralContext.class,0);
		}
		public DirectiveargContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_directivearg; }
	}

	public final DirectiveargContext directivearg() throws RecognitionException {
		DirectiveargContext _localctx = new DirectiveargContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_directivearg);
		try {
			setState(189);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(186);
				stringliteral();
				}
				break;
			case NAME:
				enterOuterAlt(_localctx, 2);
				{
				setState(187);
				identifier();
				}
				break;
			case DEC_INTEGER:
			case HEX_INTEGER:
			case BIN_INTEGER:
				enterOuterAlt(_localctx, 3);
				{
				setState(188);
				integerliteral();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VardeclContext extends ParserRuleContext {
		public DatatypeContext datatype() {
			return getRuleContext(DatatypeContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ArrayspecContext arrayspec() {
			return getRuleContext(ArrayspecContext.class,0);
		}
		public VardeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_vardecl; }
	}

	public final VardeclContext vardecl() throws RecognitionException {
		VardeclContext _localctx = new VardeclContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_vardecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(191);
			datatype();
			setState(193);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__23) {
				{
				setState(192);
				arrayspec();
				}
			}

			setState(195);
			identifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VarinitializerContext extends ParserRuleContext {
		public DatatypeContext datatype() {
			return getRuleContext(DatatypeContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ArrayspecContext arrayspec() {
			return getRuleContext(ArrayspecContext.class,0);
		}
		public VarinitializerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varinitializer; }
	}

	public final VarinitializerContext varinitializer() throws RecognitionException {
		VarinitializerContext _localctx = new VarinitializerContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_varinitializer);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(197);
			datatype();
			setState(199);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__23) {
				{
				setState(198);
				arrayspec();
				}
			}

			setState(201);
			identifier();
			setState(202);
			match(T__13);
			setState(203);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstdeclContext extends ParserRuleContext {
		public VarinitializerContext varinitializer() {
			return getRuleContext(VarinitializerContext.class,0);
		}
		public ConstdeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constdecl; }
	}

	public final ConstdeclContext constdecl() throws RecognitionException {
		ConstdeclContext _localctx = new ConstdeclContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_constdecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(205);
			match(T__14);
			setState(206);
			varinitializer();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MemoryvardeclContext extends ParserRuleContext {
		public VarinitializerContext varinitializer() {
			return getRuleContext(VarinitializerContext.class,0);
		}
		public MemoryvardeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memoryvardecl; }
	}

	public final MemoryvardeclContext memoryvardecl() throws RecognitionException {
		MemoryvardeclContext _localctx = new MemoryvardeclContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_memoryvardecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(208);
			match(T__15);
			setState(209);
			varinitializer();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DatatypeContext extends ParserRuleContext {
		public DatatypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_datatype; }
	}

	public final DatatypeContext datatype() throws RecognitionException {
		DatatypeContext _localctx = new DatatypeContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_datatype);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(211);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << T__21) | (1L << T__22))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayspecContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ArrayspecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayspec; }
	}

	public final ArrayspecContext arrayspec() throws RecognitionException {
		ArrayspecContext _localctx = new ArrayspecContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_arrayspec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(213);
			match(T__23);
			setState(214);
			expression(0);
			setState(217);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__12) {
				{
				setState(215);
				match(T__12);
				setState(216);
				expression(0);
				}
			}

			setState(219);
			match(T__24);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AssignmentContext extends ParserRuleContext {
		public Assign_targetContext assign_target() {
			return getRuleContext(Assign_targetContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public AssignmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignment; }
	}

	public final AssignmentContext assignment() throws RecognitionException {
		AssignmentContext _localctx = new AssignmentContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_assignment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(221);
			assign_target();
			setState(222);
			match(T__13);
			setState(223);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AugassignmentContext extends ParserRuleContext {
		public Token operator;
		public Assign_targetContext assign_target() {
			return getRuleContext(Assign_targetContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public AugassignmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_augassignment; }
	}

	public final AugassignmentContext augassignment() throws RecognitionException {
		AugassignmentContext _localctx = new AugassignmentContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_augassignment);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(225);
			assign_target();
			setState(226);
			((AugassignmentContext)_localctx).operator = _input.LT(1);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__25) | (1L << T__26) | (1L << T__27) | (1L << T__28) | (1L << T__29) | (1L << T__30) | (1L << T__31) | (1L << T__32) | (1L << T__33))) != 0)) ) {
				((AugassignmentContext)_localctx).operator = (Token)_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(227);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Assign_targetContext extends ParserRuleContext {
		public RegisterContext register() {
			return getRuleContext(RegisterContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public Scoped_identifierContext scoped_identifier() {
			return getRuleContext(Scoped_identifierContext.class,0);
		}
		public ArrayindexedContext arrayindexed() {
			return getRuleContext(ArrayindexedContext.class,0);
		}
		public Assign_targetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assign_target; }
	}

	public final Assign_targetContext assign_target() throws RecognitionException {
		Assign_targetContext _localctx = new Assign_targetContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_assign_target);
		try {
			setState(233);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(229);
				register();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(230);
				identifier();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(231);
				scoped_identifier();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(232);
				arrayindexed();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PostincrdecrContext extends ParserRuleContext {
		public Token operator;
		public Assign_targetContext assign_target() {
			return getRuleContext(Assign_targetContext.class,0);
		}
		public PostincrdecrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_postincrdecr; }
	}

	public final PostincrdecrContext postincrdecr() throws RecognitionException {
		PostincrdecrContext _localctx = new PostincrdecrContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_postincrdecr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(235);
			assign_target();
			setState(236);
			((PostincrdecrContext)_localctx).operator = _input.LT(1);
			_la = _input.LA(1);
			if ( !(_la==T__34 || _la==T__35) ) {
				((PostincrdecrContext)_localctx).operator = (Token)_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext left;
		public ExpressionContext rangefrom;
		public Token prefix;
		public Token bop;
		public ExpressionContext right;
		public ExpressionContext rangeto;
		public ExpressionContext rangestep;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public FunctioncallContext functioncall() {
			return getRuleContext(FunctioncallContext.class,0);
		}
		public LiteralvalueContext literalvalue() {
			return getRuleContext(LiteralvalueContext.class,0);
		}
		public RegisterContext register() {
			return getRuleContext(RegisterContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public Scoped_identifierContext scoped_identifier() {
			return getRuleContext(Scoped_identifierContext.class,0);
		}
		public ArrayindexedContext arrayindexed() {
			return getRuleContext(ArrayindexedContext.class,0);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 36;
		enterRecursionRule(_localctx, 36, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(253);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				{
				setState(239);
				match(T__36);
				setState(240);
				expression(0);
				setState(241);
				match(T__37);
				}
				break;
			case 2:
				{
				setState(243);
				functioncall();
				}
				break;
			case 3:
				{
				setState(244);
				((ExpressionContext)_localctx).prefix = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__38) | (1L << T__39))) != 0)) ) {
					((ExpressionContext)_localctx).prefix = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(245);
				expression(19);
				}
				break;
			case 4:
				{
				setState(246);
				((ExpressionContext)_localctx).prefix = match(T__59);
				setState(247);
				expression(6);
				}
				break;
			case 5:
				{
				setState(248);
				literalvalue();
				}
				break;
			case 6:
				{
				setState(249);
				register();
				}
				break;
			case 7:
				{
				setState(250);
				identifier();
				}
				break;
			case 8:
				{
				setState(251);
				scoped_identifier();
				}
				break;
			case 9:
				{
				setState(252);
				arrayindexed();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(297);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(295);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
					case 1:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.left = _prevctx;
						_localctx.left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(255);
						if (!(precpred(_ctx, 18))) throw new FailedPredicateException(this, "precpred(_ctx, 18)");
						setState(256);
						((ExpressionContext)_localctx).bop = match(T__40);
						setState(257);
						((ExpressionContext)_localctx).right = expression(19);
						}
						break;
					case 2:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.left = _prevctx;
						_localctx.left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(258);
						if (!(precpred(_ctx, 17))) throw new FailedPredicateException(this, "precpred(_ctx, 17)");
						setState(259);
						((ExpressionContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__41) | (1L << T__42) | (1L << T__43) | (1L << T__44))) != 0)) ) {
							((ExpressionContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(260);
						((ExpressionContext)_localctx).right = expression(18);
						}
						break;
					case 3:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.left = _prevctx;
						_localctx.left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(261);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(262);
						((ExpressionContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__38 || _la==T__39) ) {
							((ExpressionContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(263);
						((ExpressionContext)_localctx).right = expression(17);
						}
						break;
					case 4:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.left = _prevctx;
						_localctx.left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(264);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(265);
						((ExpressionContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__45) | (1L << T__46) | (1L << T__47) | (1L << T__48))) != 0)) ) {
							((ExpressionContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(266);
						((ExpressionContext)_localctx).right = expression(16);
						}
						break;
					case 5:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.left = _prevctx;
						_localctx.left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(267);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(268);
						((ExpressionContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__49 || _la==T__50) ) {
							((ExpressionContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(269);
						((ExpressionContext)_localctx).right = expression(15);
						}
						break;
					case 6:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.left = _prevctx;
						_localctx.left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(270);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(271);
						((ExpressionContext)_localctx).bop = match(T__51);
						setState(272);
						((ExpressionContext)_localctx).right = expression(14);
						}
						break;
					case 7:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.left = _prevctx;
						_localctx.left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(273);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(274);
						((ExpressionContext)_localctx).bop = match(T__52);
						setState(275);
						((ExpressionContext)_localctx).right = expression(13);
						}
						break;
					case 8:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.left = _prevctx;
						_localctx.left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(276);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(277);
						((ExpressionContext)_localctx).bop = match(T__53);
						setState(278);
						((ExpressionContext)_localctx).right = expression(12);
						}
						break;
					case 9:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.left = _prevctx;
						_localctx.left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(279);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(280);
						((ExpressionContext)_localctx).bop = match(T__56);
						setState(281);
						((ExpressionContext)_localctx).right = expression(10);
						}
						break;
					case 10:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.left = _prevctx;
						_localctx.left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(282);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(283);
						((ExpressionContext)_localctx).bop = match(T__57);
						setState(284);
						((ExpressionContext)_localctx).right = expression(9);
						}
						break;
					case 11:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.left = _prevctx;
						_localctx.left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(285);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(286);
						((ExpressionContext)_localctx).bop = match(T__58);
						setState(287);
						((ExpressionContext)_localctx).right = expression(8);
						}
						break;
					case 12:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.rangefrom = _prevctx;
						_localctx.rangefrom = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(288);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(289);
						match(T__54);
						setState(290);
						((ExpressionContext)_localctx).rangeto = expression(0);
						setState(293);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
						case 1:
							{
							setState(291);
							match(T__55);
							setState(292);
							((ExpressionContext)_localctx).rangestep = expression(0);
							}
							break;
						}
						}
						break;
					}
					} 
				}
				setState(299);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class ArrayindexedContext extends ParserRuleContext {
		public ArrayspecContext arrayspec() {
			return getRuleContext(ArrayspecContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public Scoped_identifierContext scoped_identifier() {
			return getRuleContext(Scoped_identifierContext.class,0);
		}
		public RegisterContext register() {
			return getRuleContext(RegisterContext.class,0);
		}
		public ArrayindexedContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayindexed; }
	}

	public final ArrayindexedContext arrayindexed() throws RecognitionException {
		ArrayindexedContext _localctx = new ArrayindexedContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_arrayindexed);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(303);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				{
				setState(300);
				identifier();
				}
				break;
			case 2:
				{
				setState(301);
				scoped_identifier();
				}
				break;
			case 3:
				{
				setState(302);
				register();
				}
				break;
			}
			setState(305);
			arrayspec();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctioncallContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public Scoped_identifierContext scoped_identifier() {
			return getRuleContext(Scoped_identifierContext.class,0);
		}
		public Expression_listContext expression_list() {
			return getRuleContext(Expression_listContext.class,0);
		}
		public FunctioncallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functioncall; }
	}

	public final FunctioncallContext functioncall() throws RecognitionException {
		FunctioncallContext _localctx = new FunctioncallContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_functioncall);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(309);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				{
				setState(307);
				identifier();
				}
				break;
			case 2:
				{
				setState(308);
				scoped_identifier();
				}
				break;
			}
			setState(311);
			match(T__36);
			setState(313);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__23) | (1L << T__36) | (1L << T__38) | (1L << T__39) | (1L << T__59))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (T__64 - 65)) | (1L << (T__65 - 65)) | (1L << (T__66 - 65)) | (1L << (T__67 - 65)) | (1L << (T__68 - 65)) | (1L << (T__69 - 65)) | (1L << (T__75 - 65)) | (1L << (T__76 - 65)) | (1L << (NAME - 65)) | (1L << (DEC_INTEGER - 65)) | (1L << (HEX_INTEGER - 65)) | (1L << (BIN_INTEGER - 65)) | (1L << (FLOAT_NUMBER - 65)) | (1L << (STRING - 65)) | (1L << (SINGLECHAR - 65)))) != 0)) {
				{
				setState(312);
				expression_list();
				}
			}

			setState(315);
			match(T__37);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Functioncall_stmtContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public Scoped_identifierContext scoped_identifier() {
			return getRuleContext(Scoped_identifierContext.class,0);
		}
		public Expression_listContext expression_list() {
			return getRuleContext(Expression_listContext.class,0);
		}
		public Functioncall_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functioncall_stmt; }
	}

	public final Functioncall_stmtContext functioncall_stmt() throws RecognitionException {
		Functioncall_stmtContext _localctx = new Functioncall_stmtContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_functioncall_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(319);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				{
				setState(317);
				identifier();
				}
				break;
			case 2:
				{
				setState(318);
				scoped_identifier();
				}
				break;
			}
			setState(321);
			match(T__36);
			setState(323);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__23) | (1L << T__36) | (1L << T__38) | (1L << T__39) | (1L << T__59))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (T__64 - 65)) | (1L << (T__65 - 65)) | (1L << (T__66 - 65)) | (1L << (T__67 - 65)) | (1L << (T__68 - 65)) | (1L << (T__69 - 65)) | (1L << (T__75 - 65)) | (1L << (T__76 - 65)) | (1L << (NAME - 65)) | (1L << (DEC_INTEGER - 65)) | (1L << (HEX_INTEGER - 65)) | (1L << (BIN_INTEGER - 65)) | (1L << (FLOAT_NUMBER - 65)) | (1L << (STRING - 65)) | (1L << (SINGLECHAR - 65)))) != 0)) {
				{
				setState(322);
				expression_list();
				}
			}

			setState(325);
			match(T__37);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Expression_listContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public Expression_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression_list; }
	}

	public final Expression_listContext expression_list() throws RecognitionException {
		Expression_listContext _localctx = new Expression_listContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_expression_list);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(327);
			expression(0);
			setState(332);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__12) {
				{
				{
				setState(328);
				match(T__12);
				setState(329);
				expression(0);
				}
				}
				setState(334);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ReturnstmtContext extends ParserRuleContext {
		public Expression_listContext expression_list() {
			return getRuleContext(Expression_listContext.class,0);
		}
		public ReturnstmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_returnstmt; }
	}

	public final ReturnstmtContext returnstmt() throws RecognitionException {
		ReturnstmtContext _localctx = new ReturnstmtContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_returnstmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(335);
			match(T__60);
			setState(337);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
			case 1:
				{
				setState(336);
				expression_list();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BreakstmtContext extends ParserRuleContext {
		public BreakstmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_breakstmt; }
	}

	public final BreakstmtContext breakstmt() throws RecognitionException {
		BreakstmtContext _localctx = new BreakstmtContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_breakstmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(339);
			match(T__61);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ContinuestmtContext extends ParserRuleContext {
		public ContinuestmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_continuestmt; }
	}

	public final ContinuestmtContext continuestmt() throws RecognitionException {
		ContinuestmtContext _localctx = new ContinuestmtContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_continuestmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(341);
			match(T__62);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdentifierContext extends ParserRuleContext {
		public TerminalNode NAME() { return getToken(prog8Parser.NAME, 0); }
		public IdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifier; }
	}

	public final IdentifierContext identifier() throws RecognitionException {
		IdentifierContext _localctx = new IdentifierContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_identifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(343);
			match(NAME);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Scoped_identifierContext extends ParserRuleContext {
		public List<TerminalNode> NAME() { return getTokens(prog8Parser.NAME); }
		public TerminalNode NAME(int i) {
			return getToken(prog8Parser.NAME, i);
		}
		public Scoped_identifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_scoped_identifier; }
	}

	public final Scoped_identifierContext scoped_identifier() throws RecognitionException {
		Scoped_identifierContext _localctx = new Scoped_identifierContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_scoped_identifier);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(345);
			match(NAME);
			setState(348); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(346);
					match(T__63);
					setState(347);
					match(NAME);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(350); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RegisterContext extends ParserRuleContext {
		public RegisterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_register; }
	}

	public final RegisterContext register() throws RecognitionException {
		RegisterContext _localctx = new RegisterContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_register);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(352);
			_la = _input.LA(1);
			if ( !(((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (T__64 - 65)) | (1L << (T__65 - 65)) | (1L << (T__66 - 65)) | (1L << (T__67 - 65)) | (1L << (T__68 - 65)) | (1L << (T__69 - 65)))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatusregisterContext extends ParserRuleContext {
		public StatusregisterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statusregister; }
	}

	public final StatusregisterContext statusregister() throws RecognitionException {
		StatusregisterContext _localctx = new StatusregisterContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_statusregister);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(354);
			_la = _input.LA(1);
			if ( !(((((_la - 71)) & ~0x3f) == 0 && ((1L << (_la - 71)) & ((1L << (T__70 - 71)) | (1L << (T__71 - 71)) | (1L << (T__72 - 71)) | (1L << (T__73 - 71)))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IntegerliteralContext extends ParserRuleContext {
		public Token intpart;
		public TerminalNode DEC_INTEGER() { return getToken(prog8Parser.DEC_INTEGER, 0); }
		public TerminalNode HEX_INTEGER() { return getToken(prog8Parser.HEX_INTEGER, 0); }
		public TerminalNode BIN_INTEGER() { return getToken(prog8Parser.BIN_INTEGER, 0); }
		public WordsuffixContext wordsuffix() {
			return getRuleContext(WordsuffixContext.class,0);
		}
		public IntegerliteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_integerliteral; }
	}

	public final IntegerliteralContext integerliteral() throws RecognitionException {
		IntegerliteralContext _localctx = new IntegerliteralContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_integerliteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(356);
			((IntegerliteralContext)_localctx).intpart = _input.LT(1);
			_la = _input.LA(1);
			if ( !(((((_la - 110)) & ~0x3f) == 0 && ((1L << (_la - 110)) & ((1L << (DEC_INTEGER - 110)) | (1L << (HEX_INTEGER - 110)) | (1L << (BIN_INTEGER - 110)))) != 0)) ) {
				((IntegerliteralContext)_localctx).intpart = (Token)_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(358);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
			case 1:
				{
				setState(357);
				wordsuffix();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WordsuffixContext extends ParserRuleContext {
		public WordsuffixContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wordsuffix; }
	}

	public final WordsuffixContext wordsuffix() throws RecognitionException {
		WordsuffixContext _localctx = new WordsuffixContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_wordsuffix);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(360);
			match(T__74);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BooleanliteralContext extends ParserRuleContext {
		public BooleanliteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleanliteral; }
	}

	public final BooleanliteralContext booleanliteral() throws RecognitionException {
		BooleanliteralContext _localctx = new BooleanliteralContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_booleanliteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(362);
			_la = _input.LA(1);
			if ( !(_la==T__75 || _la==T__76) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayliteralContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ArrayliteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayliteral; }
	}

	public final ArrayliteralContext arrayliteral() throws RecognitionException {
		ArrayliteralContext _localctx = new ArrayliteralContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_arrayliteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(364);
			match(T__23);
			setState(365);
			expression(0);
			setState(370);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__12) {
				{
				{
				setState(366);
				match(T__12);
				setState(367);
				expression(0);
				}
				}
				setState(372);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(373);
			match(T__24);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StringliteralContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(prog8Parser.STRING, 0); }
		public StringliteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringliteral; }
	}

	public final StringliteralContext stringliteral() throws RecognitionException {
		StringliteralContext _localctx = new StringliteralContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_stringliteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(375);
			match(STRING);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CharliteralContext extends ParserRuleContext {
		public TerminalNode SINGLECHAR() { return getToken(prog8Parser.SINGLECHAR, 0); }
		public CharliteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_charliteral; }
	}

	public final CharliteralContext charliteral() throws RecognitionException {
		CharliteralContext _localctx = new CharliteralContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_charliteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(377);
			match(SINGLECHAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FloatliteralContext extends ParserRuleContext {
		public TerminalNode FLOAT_NUMBER() { return getToken(prog8Parser.FLOAT_NUMBER, 0); }
		public FloatliteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_floatliteral; }
	}

	public final FloatliteralContext floatliteral() throws RecognitionException {
		FloatliteralContext _localctx = new FloatliteralContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_floatliteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(379);
			match(FLOAT_NUMBER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LiteralvalueContext extends ParserRuleContext {
		public IntegerliteralContext integerliteral() {
			return getRuleContext(IntegerliteralContext.class,0);
		}
		public BooleanliteralContext booleanliteral() {
			return getRuleContext(BooleanliteralContext.class,0);
		}
		public ArrayliteralContext arrayliteral() {
			return getRuleContext(ArrayliteralContext.class,0);
		}
		public StringliteralContext stringliteral() {
			return getRuleContext(StringliteralContext.class,0);
		}
		public CharliteralContext charliteral() {
			return getRuleContext(CharliteralContext.class,0);
		}
		public FloatliteralContext floatliteral() {
			return getRuleContext(FloatliteralContext.class,0);
		}
		public LiteralvalueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literalvalue; }
	}

	public final LiteralvalueContext literalvalue() throws RecognitionException {
		LiteralvalueContext _localctx = new LiteralvalueContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_literalvalue);
		try {
			setState(387);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DEC_INTEGER:
			case HEX_INTEGER:
			case BIN_INTEGER:
				enterOuterAlt(_localctx, 1);
				{
				setState(381);
				integerliteral();
				}
				break;
			case T__75:
			case T__76:
				enterOuterAlt(_localctx, 2);
				{
				setState(382);
				booleanliteral();
				}
				break;
			case T__23:
				enterOuterAlt(_localctx, 3);
				{
				setState(383);
				arrayliteral();
				}
				break;
			case STRING:
				enterOuterAlt(_localctx, 4);
				{
				setState(384);
				stringliteral();
				}
				break;
			case SINGLECHAR:
				enterOuterAlt(_localctx, 5);
				{
				setState(385);
				charliteral();
				}
				break;
			case FLOAT_NUMBER:
				enterOuterAlt(_localctx, 6);
				{
				setState(386);
				floatliteral();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InlineasmContext extends ParserRuleContext {
		public TerminalNode INLINEASMBLOCK() { return getToken(prog8Parser.INLINEASMBLOCK, 0); }
		public InlineasmContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inlineasm; }
	}

	public final InlineasmContext inlineasm() throws RecognitionException {
		InlineasmContext _localctx = new InlineasmContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_inlineasm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(389);
			match(T__77);
			setState(390);
			match(INLINEASMBLOCK);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SubroutineContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public Statement_blockContext statement_block() {
			return getRuleContext(Statement_blockContext.class,0);
		}
		public TerminalNode EOL() { return getToken(prog8Parser.EOL, 0); }
		public Sub_paramsContext sub_params() {
			return getRuleContext(Sub_paramsContext.class,0);
		}
		public Sub_return_partContext sub_return_part() {
			return getRuleContext(Sub_return_partContext.class,0);
		}
		public SubroutineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_subroutine; }
	}

	public final SubroutineContext subroutine() throws RecognitionException {
		SubroutineContext _localctx = new SubroutineContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_subroutine);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(392);
			match(T__78);
			setState(393);
			identifier();
			setState(394);
			match(T__36);
			setState(396);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NAME) {
				{
				setState(395);
				sub_params();
				}
			}

			setState(398);
			match(T__37);
			setState(400);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__79) {
				{
				setState(399);
				sub_return_part();
				}
			}

			{
			setState(402);
			statement_block();
			setState(403);
			match(EOL);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Sub_return_partContext extends ParserRuleContext {
		public Sub_returnsContext sub_returns() {
			return getRuleContext(Sub_returnsContext.class,0);
		}
		public Sub_return_partContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sub_return_part; }
	}

	public final Sub_return_partContext sub_return_part() throws RecognitionException {
		Sub_return_partContext _localctx = new Sub_return_partContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_sub_return_part);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(405);
			match(T__79);
			setState(406);
			sub_returns();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Statement_blockContext extends ParserRuleContext {
		public List<TerminalNode> EOL() { return getTokens(prog8Parser.EOL); }
		public TerminalNode EOL(int i) {
			return getToken(prog8Parser.EOL, i);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public Statement_blockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement_block; }
	}

	public final Statement_blockContext statement_block() throws RecognitionException {
		Statement_blockContext _localctx = new Statement_blockContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_statement_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(408);
			match(T__80);
			setState(409);
			match(EOL);
			setState(414);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << T__21) | (1L << T__22) | (1L << T__60) | (1L << T__61) | (1L << T__62))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (T__64 - 65)) | (1L << (T__65 - 65)) | (1L << (T__66 - 65)) | (1L << (T__67 - 65)) | (1L << (T__68 - 65)) | (1L << (T__69 - 65)) | (1L << (T__77 - 65)) | (1L << (T__78 - 65)) | (1L << (T__82 - 65)) | (1L << (T__85 - 65)) | (1L << (T__87 - 65)) | (1L << (T__88 - 65)) | (1L << (T__89 - 65)) | (1L << (T__90 - 65)) | (1L << (T__91 - 65)) | (1L << (T__92 - 65)) | (1L << (T__93 - 65)) | (1L << (T__94 - 65)) | (1L << (T__95 - 65)) | (1L << (T__96 - 65)) | (1L << (T__97 - 65)) | (1L << (T__98 - 65)) | (1L << (T__99 - 65)) | (1L << (T__101 - 65)) | (1L << (T__102 - 65)) | (1L << (EOL - 65)) | (1L << (NAME - 65)))) != 0)) {
				{
				setState(412);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__2:
				case T__3:
				case T__4:
				case T__5:
				case T__6:
				case T__7:
				case T__8:
				case T__9:
				case T__10:
				case T__11:
				case T__14:
				case T__15:
				case T__16:
				case T__17:
				case T__18:
				case T__19:
				case T__20:
				case T__21:
				case T__22:
				case T__60:
				case T__61:
				case T__62:
				case T__64:
				case T__65:
				case T__66:
				case T__67:
				case T__68:
				case T__69:
				case T__77:
				case T__78:
				case T__82:
				case T__85:
				case T__87:
				case T__88:
				case T__89:
				case T__90:
				case T__91:
				case T__92:
				case T__93:
				case T__94:
				case T__95:
				case T__96:
				case T__97:
				case T__98:
				case T__99:
				case T__101:
				case T__102:
				case NAME:
					{
					setState(410);
					statement();
					}
					break;
				case EOL:
					{
					setState(411);
					match(EOL);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(416);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(417);
			match(T__81);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Sub_paramsContext extends ParserRuleContext {
		public List<Sub_paramContext> sub_param() {
			return getRuleContexts(Sub_paramContext.class);
		}
		public Sub_paramContext sub_param(int i) {
			return getRuleContext(Sub_paramContext.class,i);
		}
		public Sub_paramsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sub_params; }
	}

	public final Sub_paramsContext sub_params() throws RecognitionException {
		Sub_paramsContext _localctx = new Sub_paramsContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_sub_params);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(419);
			sub_param();
			setState(424);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__12) {
				{
				{
				setState(420);
				match(T__12);
				setState(421);
				sub_param();
				}
				}
				setState(426);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Sub_paramContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public DatatypeContext datatype() {
			return getRuleContext(DatatypeContext.class,0);
		}
		public Sub_paramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sub_param; }
	}

	public final Sub_paramContext sub_param() throws RecognitionException {
		Sub_paramContext _localctx = new Sub_paramContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_sub_param);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(427);
			identifier();
			setState(428);
			match(T__1);
			setState(429);
			datatype();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Sub_returnsContext extends ParserRuleContext {
		public List<DatatypeContext> datatype() {
			return getRuleContexts(DatatypeContext.class);
		}
		public DatatypeContext datatype(int i) {
			return getRuleContext(DatatypeContext.class,i);
		}
		public Sub_returnsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sub_returns; }
	}

	public final Sub_returnsContext sub_returns() throws RecognitionException {
		Sub_returnsContext _localctx = new Sub_returnsContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_sub_returns);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(431);
			datatype();
			setState(436);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__12) {
				{
				{
				setState(432);
				match(T__12);
				setState(433);
				datatype();
				}
				}
				setState(438);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AsmsubroutineContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public Asmsub_addressContext asmsub_address() {
			return getRuleContext(Asmsub_addressContext.class,0);
		}
		public Statement_blockContext statement_block() {
			return getRuleContext(Statement_blockContext.class,0);
		}
		public Asmsub_paramsContext asmsub_params() {
			return getRuleContext(Asmsub_paramsContext.class,0);
		}
		public ClobberContext clobber() {
			return getRuleContext(ClobberContext.class,0);
		}
		public Asmsub_returnsContext asmsub_returns() {
			return getRuleContext(Asmsub_returnsContext.class,0);
		}
		public AsmsubroutineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_asmsubroutine; }
	}

	public final AsmsubroutineContext asmsubroutine() throws RecognitionException {
		AsmsubroutineContext _localctx = new AsmsubroutineContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_asmsubroutine);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(439);
			match(T__82);
			setState(440);
			identifier();
			setState(441);
			match(T__36);
			setState(443);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NAME) {
				{
				setState(442);
				asmsub_params();
				}
			}

			setState(445);
			match(T__37);
			setState(446);
			match(T__79);
			setState(447);
			match(T__83);
			setState(448);
			match(T__36);
			setState(450);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (T__64 - 65)) | (1L << (T__65 - 65)) | (1L << (T__66 - 65)) | (1L << (T__67 - 65)) | (1L << (T__68 - 65)) | (1L << (T__69 - 65)))) != 0)) {
				{
				setState(449);
				clobber();
				}
			}

			setState(452);
			match(T__37);
			setState(453);
			match(T__79);
			setState(454);
			match(T__36);
			setState(456);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << T__21) | (1L << T__22))) != 0)) {
				{
				setState(455);
				asmsub_returns();
				}
			}

			setState(458);
			match(T__37);
			setState(461);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__13:
				{
				setState(459);
				asmsub_address();
				}
				break;
			case T__80:
				{
				setState(460);
				statement_block();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Asmsub_addressContext extends ParserRuleContext {
		public IntegerliteralContext address;
		public IntegerliteralContext integerliteral() {
			return getRuleContext(IntegerliteralContext.class,0);
		}
		public Asmsub_addressContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_asmsub_address; }
	}

	public final Asmsub_addressContext asmsub_address() throws RecognitionException {
		Asmsub_addressContext _localctx = new Asmsub_addressContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_asmsub_address);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(463);
			match(T__13);
			setState(464);
			((Asmsub_addressContext)_localctx).address = integerliteral();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Asmsub_paramsContext extends ParserRuleContext {
		public List<Asmsub_paramContext> asmsub_param() {
			return getRuleContexts(Asmsub_paramContext.class);
		}
		public Asmsub_paramContext asmsub_param(int i) {
			return getRuleContext(Asmsub_paramContext.class,i);
		}
		public Asmsub_paramsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_asmsub_params; }
	}

	public final Asmsub_paramsContext asmsub_params() throws RecognitionException {
		Asmsub_paramsContext _localctx = new Asmsub_paramsContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_asmsub_params);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(466);
			asmsub_param();
			setState(471);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__12) {
				{
				{
				setState(467);
				match(T__12);
				setState(468);
				asmsub_param();
				}
				}
				setState(473);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Asmsub_paramContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public DatatypeContext datatype() {
			return getRuleContext(DatatypeContext.class,0);
		}
		public RegisterContext register() {
			return getRuleContext(RegisterContext.class,0);
		}
		public StatusregisterContext statusregister() {
			return getRuleContext(StatusregisterContext.class,0);
		}
		public Asmsub_paramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_asmsub_param; }
	}

	public final Asmsub_paramContext asmsub_param() throws RecognitionException {
		Asmsub_paramContext _localctx = new Asmsub_paramContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_asmsub_param);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(474);
			identifier();
			setState(475);
			match(T__1);
			setState(476);
			datatype();
			setState(477);
			match(T__84);
			setState(480);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__64:
			case T__65:
			case T__66:
			case T__67:
			case T__68:
			case T__69:
				{
				setState(478);
				register();
				}
				break;
			case T__70:
			case T__71:
			case T__72:
			case T__73:
				{
				setState(479);
				statusregister();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClobberContext extends ParserRuleContext {
		public List<RegisterContext> register() {
			return getRuleContexts(RegisterContext.class);
		}
		public RegisterContext register(int i) {
			return getRuleContext(RegisterContext.class,i);
		}
		public ClobberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_clobber; }
	}

	public final ClobberContext clobber() throws RecognitionException {
		ClobberContext _localctx = new ClobberContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_clobber);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(482);
			register();
			setState(487);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__12) {
				{
				{
				setState(483);
				match(T__12);
				setState(484);
				register();
				}
				}
				setState(489);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Asmsub_returnsContext extends ParserRuleContext {
		public List<Asmsub_returnContext> asmsub_return() {
			return getRuleContexts(Asmsub_returnContext.class);
		}
		public Asmsub_returnContext asmsub_return(int i) {
			return getRuleContext(Asmsub_returnContext.class,i);
		}
		public Asmsub_returnsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_asmsub_returns; }
	}

	public final Asmsub_returnsContext asmsub_returns() throws RecognitionException {
		Asmsub_returnsContext _localctx = new Asmsub_returnsContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_asmsub_returns);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(490);
			asmsub_return();
			setState(495);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__12) {
				{
				{
				setState(491);
				match(T__12);
				setState(492);
				asmsub_return();
				}
				}
				setState(497);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Asmsub_returnContext extends ParserRuleContext {
		public DatatypeContext datatype() {
			return getRuleContext(DatatypeContext.class,0);
		}
		public RegisterContext register() {
			return getRuleContext(RegisterContext.class,0);
		}
		public StatusregisterContext statusregister() {
			return getRuleContext(StatusregisterContext.class,0);
		}
		public Asmsub_returnContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_asmsub_return; }
	}

	public final Asmsub_returnContext asmsub_return() throws RecognitionException {
		Asmsub_returnContext _localctx = new Asmsub_returnContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_asmsub_return);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(498);
			datatype();
			setState(499);
			match(T__84);
			setState(502);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__64:
			case T__65:
			case T__66:
			case T__67:
			case T__68:
			case T__69:
				{
				setState(500);
				register();
				}
				break;
			case T__70:
			case T__71:
			case T__72:
			case T__73:
				{
				setState(501);
				statusregister();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class If_stmtContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<TerminalNode> EOL() { return getTokens(prog8Parser.EOL); }
		public TerminalNode EOL(int i) {
			return getToken(prog8Parser.EOL, i);
		}
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public Statement_blockContext statement_block() {
			return getRuleContext(Statement_blockContext.class,0);
		}
		public Else_partContext else_part() {
			return getRuleContext(Else_partContext.class,0);
		}
		public If_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_if_stmt; }
	}

	public final If_stmtContext if_stmt() throws RecognitionException {
		If_stmtContext _localctx = new If_stmtContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_if_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(504);
			match(T__85);
			setState(505);
			expression(0);
			setState(507);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EOL) {
				{
				setState(506);
				match(EOL);
				}
			}

			setState(511);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__2:
			case T__3:
			case T__4:
			case T__5:
			case T__6:
			case T__7:
			case T__8:
			case T__9:
			case T__10:
			case T__11:
			case T__14:
			case T__15:
			case T__16:
			case T__17:
			case T__18:
			case T__19:
			case T__20:
			case T__21:
			case T__22:
			case T__60:
			case T__61:
			case T__62:
			case T__64:
			case T__65:
			case T__66:
			case T__67:
			case T__68:
			case T__69:
			case T__77:
			case T__78:
			case T__82:
			case T__85:
			case T__87:
			case T__88:
			case T__89:
			case T__90:
			case T__91:
			case T__92:
			case T__93:
			case T__94:
			case T__95:
			case T__96:
			case T__97:
			case T__98:
			case T__99:
			case T__101:
			case T__102:
			case NAME:
				{
				setState(509);
				statement();
				}
				break;
			case T__80:
				{
				setState(510);
				statement_block();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(514);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,46,_ctx) ) {
			case 1:
				{
				setState(513);
				match(EOL);
				}
				break;
			}
			setState(517);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__86) {
				{
				setState(516);
				else_part();
				}
			}

			setState(519);
			match(EOL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Else_partContext extends ParserRuleContext {
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public Statement_blockContext statement_block() {
			return getRuleContext(Statement_blockContext.class,0);
		}
		public TerminalNode EOL() { return getToken(prog8Parser.EOL, 0); }
		public Else_partContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_else_part; }
	}

	public final Else_partContext else_part() throws RecognitionException {
		Else_partContext _localctx = new Else_partContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_else_part);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(521);
			match(T__86);
			setState(523);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EOL) {
				{
				setState(522);
				match(EOL);
				}
			}

			setState(527);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__2:
			case T__3:
			case T__4:
			case T__5:
			case T__6:
			case T__7:
			case T__8:
			case T__9:
			case T__10:
			case T__11:
			case T__14:
			case T__15:
			case T__16:
			case T__17:
			case T__18:
			case T__19:
			case T__20:
			case T__21:
			case T__22:
			case T__60:
			case T__61:
			case T__62:
			case T__64:
			case T__65:
			case T__66:
			case T__67:
			case T__68:
			case T__69:
			case T__77:
			case T__78:
			case T__82:
			case T__85:
			case T__87:
			case T__88:
			case T__89:
			case T__90:
			case T__91:
			case T__92:
			case T__93:
			case T__94:
			case T__95:
			case T__96:
			case T__97:
			case T__98:
			case T__99:
			case T__101:
			case T__102:
			case NAME:
				{
				setState(525);
				statement();
				}
				break;
			case T__80:
				{
				setState(526);
				statement_block();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Branch_stmtContext extends ParserRuleContext {
		public BranchconditionContext branchcondition() {
			return getRuleContext(BranchconditionContext.class,0);
		}
		public List<TerminalNode> EOL() { return getTokens(prog8Parser.EOL); }
		public TerminalNode EOL(int i) {
			return getToken(prog8Parser.EOL, i);
		}
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public Statement_blockContext statement_block() {
			return getRuleContext(Statement_blockContext.class,0);
		}
		public Else_partContext else_part() {
			return getRuleContext(Else_partContext.class,0);
		}
		public Branch_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_branch_stmt; }
	}

	public final Branch_stmtContext branch_stmt() throws RecognitionException {
		Branch_stmtContext _localctx = new Branch_stmtContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_branch_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(529);
			branchcondition();
			setState(531);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EOL) {
				{
				setState(530);
				match(EOL);
				}
			}

			setState(535);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__2:
			case T__3:
			case T__4:
			case T__5:
			case T__6:
			case T__7:
			case T__8:
			case T__9:
			case T__10:
			case T__11:
			case T__14:
			case T__15:
			case T__16:
			case T__17:
			case T__18:
			case T__19:
			case T__20:
			case T__21:
			case T__22:
			case T__60:
			case T__61:
			case T__62:
			case T__64:
			case T__65:
			case T__66:
			case T__67:
			case T__68:
			case T__69:
			case T__77:
			case T__78:
			case T__82:
			case T__85:
			case T__87:
			case T__88:
			case T__89:
			case T__90:
			case T__91:
			case T__92:
			case T__93:
			case T__94:
			case T__95:
			case T__96:
			case T__97:
			case T__98:
			case T__99:
			case T__101:
			case T__102:
			case NAME:
				{
				setState(533);
				statement();
				}
				break;
			case T__80:
				{
				setState(534);
				statement_block();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(538);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,52,_ctx) ) {
			case 1:
				{
				setState(537);
				match(EOL);
				}
				break;
			}
			setState(541);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__86) {
				{
				setState(540);
				else_part();
				}
			}

			setState(543);
			match(EOL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BranchconditionContext extends ParserRuleContext {
		public BranchconditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_branchcondition; }
	}

	public final BranchconditionContext branchcondition() throws RecognitionException {
		BranchconditionContext _localctx = new BranchconditionContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_branchcondition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(545);
			_la = _input.LA(1);
			if ( !(((((_la - 88)) & ~0x3f) == 0 && ((1L << (_la - 88)) & ((1L << (T__87 - 88)) | (1L << (T__88 - 88)) | (1L << (T__89 - 88)) | (1L << (T__90 - 88)) | (1L << (T__91 - 88)) | (1L << (T__92 - 88)) | (1L << (T__93 - 88)) | (1L << (T__94 - 88)) | (1L << (T__95 - 88)) | (1L << (T__96 - 88)) | (1L << (T__97 - 88)) | (1L << (T__98 - 88)))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ForloopContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public Statement_blockContext statement_block() {
			return getRuleContext(Statement_blockContext.class,0);
		}
		public RegisterContext register() {
			return getRuleContext(RegisterContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode EOL() { return getToken(prog8Parser.EOL, 0); }
		public ForloopContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forloop; }
	}

	public final ForloopContext forloop() throws RecognitionException {
		ForloopContext _localctx = new ForloopContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_forloop);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(547);
			match(T__99);
			setState(550);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__64:
			case T__65:
			case T__66:
			case T__67:
			case T__68:
			case T__69:
				{
				setState(548);
				register();
				}
				break;
			case NAME:
				{
				setState(549);
				identifier();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(552);
			match(T__100);
			setState(553);
			expression(0);
			setState(555);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EOL) {
				{
				setState(554);
				match(EOL);
				}
			}

			setState(557);
			statement_block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WhileloopContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public Statement_blockContext statement_block() {
			return getRuleContext(Statement_blockContext.class,0);
		}
		public TerminalNode EOL() { return getToken(prog8Parser.EOL, 0); }
		public WhileloopContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whileloop; }
	}

	public final WhileloopContext whileloop() throws RecognitionException {
		WhileloopContext _localctx = new WhileloopContext(_ctx, getState());
		enterRule(_localctx, 114, RULE_whileloop);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(559);
			match(T__101);
			setState(560);
			expression(0);
			setState(562);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EOL) {
				{
				setState(561);
				match(EOL);
				}
			}

			setState(566);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__2:
			case T__3:
			case T__4:
			case T__5:
			case T__6:
			case T__7:
			case T__8:
			case T__9:
			case T__10:
			case T__11:
			case T__14:
			case T__15:
			case T__16:
			case T__17:
			case T__18:
			case T__19:
			case T__20:
			case T__21:
			case T__22:
			case T__60:
			case T__61:
			case T__62:
			case T__64:
			case T__65:
			case T__66:
			case T__67:
			case T__68:
			case T__69:
			case T__77:
			case T__78:
			case T__82:
			case T__85:
			case T__87:
			case T__88:
			case T__89:
			case T__90:
			case T__91:
			case T__92:
			case T__93:
			case T__94:
			case T__95:
			case T__96:
			case T__97:
			case T__98:
			case T__99:
			case T__101:
			case T__102:
			case NAME:
				{
				setState(564);
				statement();
				}
				break;
			case T__80:
				{
				setState(565);
				statement_block();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RepeatloopContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public Statement_blockContext statement_block() {
			return getRuleContext(Statement_blockContext.class,0);
		}
		public TerminalNode EOL() { return getToken(prog8Parser.EOL, 0); }
		public RepeatloopContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_repeatloop; }
	}

	public final RepeatloopContext repeatloop() throws RecognitionException {
		RepeatloopContext _localctx = new RepeatloopContext(_ctx, getState());
		enterRule(_localctx, 116, RULE_repeatloop);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(568);
			match(T__102);
			setState(571);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__2:
			case T__3:
			case T__4:
			case T__5:
			case T__6:
			case T__7:
			case T__8:
			case T__9:
			case T__10:
			case T__11:
			case T__14:
			case T__15:
			case T__16:
			case T__17:
			case T__18:
			case T__19:
			case T__20:
			case T__21:
			case T__22:
			case T__60:
			case T__61:
			case T__62:
			case T__64:
			case T__65:
			case T__66:
			case T__67:
			case T__68:
			case T__69:
			case T__77:
			case T__78:
			case T__82:
			case T__85:
			case T__87:
			case T__88:
			case T__89:
			case T__90:
			case T__91:
			case T__92:
			case T__93:
			case T__94:
			case T__95:
			case T__96:
			case T__97:
			case T__98:
			case T__99:
			case T__101:
			case T__102:
			case NAME:
				{
				setState(569);
				statement();
				}
				break;
			case T__80:
				{
				setState(570);
				statement_block();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(574);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EOL) {
				{
				setState(573);
				match(EOL);
				}
			}

			setState(576);
			match(T__103);
			setState(577);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 18:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 18);
		case 1:
			return precpred(_ctx, 17);
		case 2:
			return precpred(_ctx, 16);
		case 3:
			return precpred(_ctx, 15);
		case 4:
			return precpred(_ctx, 14);
		case 5:
			return precpred(_ctx, 13);
		case 6:
			return precpred(_ctx, 12);
		case 7:
			return precpred(_ctx, 11);
		case 8:
			return precpred(_ctx, 9);
		case 9:
			return precpred(_ctx, 8);
		case 10:
			return precpred(_ctx, 7);
		case 11:
			return precpred(_ctx, 10);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3v\u0246\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\3\2\3"+
		"\2\7\2{\n\2\f\2\16\2~\13\2\3\2\3\2\3\3\3\3\5\3\u0084\n\3\3\4\3\4\3\4\5"+
		"\4\u0089\n\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3"+
		"\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\5\5\u00a4\n\5\3\6\3\6\3\6\3"+
		"\7\3\7\3\7\3\7\5\7\u00ad\n\7\3\b\3\b\5\b\u00b1\n\b\3\b\3\b\3\b\7\b\u00b6"+
		"\n\b\f\b\16\b\u00b9\13\b\5\b\u00bb\n\b\3\t\3\t\3\t\5\t\u00c0\n\t\3\n\3"+
		"\n\5\n\u00c4\n\n\3\n\3\n\3\13\3\13\5\13\u00ca\n\13\3\13\3\13\3\13\3\13"+
		"\3\f\3\f\3\f\3\r\3\r\3\r\3\16\3\16\3\17\3\17\3\17\3\17\5\17\u00dc\n\17"+
		"\3\17\3\17\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22"+
		"\5\22\u00ec\n\22\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24"+
		"\3\24\3\24\3\24\3\24\3\24\3\24\3\24\5\24\u0100\n\24\3\24\3\24\3\24\3\24"+
		"\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24"+
		"\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24"+
		"\3\24\3\24\3\24\3\24\3\24\3\24\5\24\u0128\n\24\7\24\u012a\n\24\f\24\16"+
		"\24\u012d\13\24\3\25\3\25\3\25\5\25\u0132\n\25\3\25\3\25\3\26\3\26\5\26"+
		"\u0138\n\26\3\26\3\26\5\26\u013c\n\26\3\26\3\26\3\27\3\27\5\27\u0142\n"+
		"\27\3\27\3\27\5\27\u0146\n\27\3\27\3\27\3\30\3\30\3\30\7\30\u014d\n\30"+
		"\f\30\16\30\u0150\13\30\3\31\3\31\5\31\u0154\n\31\3\32\3\32\3\33\3\33"+
		"\3\34\3\34\3\35\3\35\3\35\6\35\u015f\n\35\r\35\16\35\u0160\3\36\3\36\3"+
		"\37\3\37\3 \3 \5 \u0169\n \3!\3!\3\"\3\"\3#\3#\3#\3#\7#\u0173\n#\f#\16"+
		"#\u0176\13#\3#\3#\3$\3$\3%\3%\3&\3&\3\'\3\'\3\'\3\'\3\'\3\'\5\'\u0186"+
		"\n\'\3(\3(\3(\3)\3)\3)\3)\5)\u018f\n)\3)\3)\5)\u0193\n)\3)\3)\3)\3*\3"+
		"*\3*\3+\3+\3+\3+\7+\u019f\n+\f+\16+\u01a2\13+\3+\3+\3,\3,\3,\7,\u01a9"+
		"\n,\f,\16,\u01ac\13,\3-\3-\3-\3-\3.\3.\3.\7.\u01b5\n.\f.\16.\u01b8\13"+
		".\3/\3/\3/\3/\5/\u01be\n/\3/\3/\3/\3/\3/\5/\u01c5\n/\3/\3/\3/\3/\5/\u01cb"+
		"\n/\3/\3/\3/\5/\u01d0\n/\3\60\3\60\3\60\3\61\3\61\3\61\7\61\u01d8\n\61"+
		"\f\61\16\61\u01db\13\61\3\62\3\62\3\62\3\62\3\62\3\62\5\62\u01e3\n\62"+
		"\3\63\3\63\3\63\7\63\u01e8\n\63\f\63\16\63\u01eb\13\63\3\64\3\64\3\64"+
		"\7\64\u01f0\n\64\f\64\16\64\u01f3\13\64\3\65\3\65\3\65\3\65\5\65\u01f9"+
		"\n\65\3\66\3\66\3\66\5\66\u01fe\n\66\3\66\3\66\5\66\u0202\n\66\3\66\5"+
		"\66\u0205\n\66\3\66\5\66\u0208\n\66\3\66\3\66\3\67\3\67\5\67\u020e\n\67"+
		"\3\67\3\67\5\67\u0212\n\67\38\38\58\u0216\n8\38\38\58\u021a\n8\38\58\u021d"+
		"\n8\38\58\u0220\n8\38\38\39\39\3:\3:\3:\5:\u0229\n:\3:\3:\3:\5:\u022e"+
		"\n:\3:\3:\3;\3;\3;\5;\u0235\n;\3;\3;\5;\u0239\n;\3<\3<\3<\5<\u023e\n<"+
		"\3<\5<\u0241\n<\3<\3<\3<\3<\2\3&=\2\4\6\b\n\f\16\20\22\24\26\30\32\34"+
		"\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtv\2\20\3\2\6\16"+
		"\3\2\23\31\3\2\34$\3\2%&\4\2\3\3)*\3\2,/\3\2)*\3\2\60\63\3\2\64\65\3\2"+
		"CH\3\2IL\3\2pr\3\2NO\3\2Ze\2\u0274\2|\3\2\2\2\4\u0083\3\2\2\2\6\u0085"+
		"\3\2\2\2\b\u00a3\3\2\2\2\n\u00a5\3\2\2\2\f\u00a8\3\2\2\2\16\u00ae\3\2"+
		"\2\2\20\u00bf\3\2\2\2\22\u00c1\3\2\2\2\24\u00c7\3\2\2\2\26\u00cf\3\2\2"+
		"\2\30\u00d2\3\2\2\2\32\u00d5\3\2\2\2\34\u00d7\3\2\2\2\36\u00df\3\2\2\2"+
		" \u00e3\3\2\2\2\"\u00eb\3\2\2\2$\u00ed\3\2\2\2&\u00ff\3\2\2\2(\u0131\3"+
		"\2\2\2*\u0137\3\2\2\2,\u0141\3\2\2\2.\u0149\3\2\2\2\60\u0151\3\2\2\2\62"+
		"\u0155\3\2\2\2\64\u0157\3\2\2\2\66\u0159\3\2\2\28\u015b\3\2\2\2:\u0162"+
		"\3\2\2\2<\u0164\3\2\2\2>\u0166\3\2\2\2@\u016a\3\2\2\2B\u016c\3\2\2\2D"+
		"\u016e\3\2\2\2F\u0179\3\2\2\2H\u017b\3\2\2\2J\u017d\3\2\2\2L\u0185\3\2"+
		"\2\2N\u0187\3\2\2\2P\u018a\3\2\2\2R\u0197\3\2\2\2T\u019a\3\2\2\2V\u01a5"+
		"\3\2\2\2X\u01ad\3\2\2\2Z\u01b1\3\2\2\2\\\u01b9\3\2\2\2^\u01d1\3\2\2\2"+
		"`\u01d4\3\2\2\2b\u01dc\3\2\2\2d\u01e4\3\2\2\2f\u01ec\3\2\2\2h\u01f4\3"+
		"\2\2\2j\u01fa\3\2\2\2l\u020b\3\2\2\2n\u0213\3\2\2\2p\u0223\3\2\2\2r\u0225"+
		"\3\2\2\2t\u0231\3\2\2\2v\u023a\3\2\2\2x{\5\4\3\2y{\7n\2\2zx\3\2\2\2zy"+
		"\3\2\2\2{~\3\2\2\2|z\3\2\2\2|}\3\2\2\2}\177\3\2\2\2~|\3\2\2\2\177\u0080"+
		"\7\2\2\3\u0080\3\3\2\2\2\u0081\u0084\5\16\b\2\u0082\u0084\5\6\4\2\u0083"+
		"\u0081\3\2\2\2\u0083\u0082\3\2\2\2\u0084\5\3\2\2\2\u0085\u0086\7\3\2\2"+
		"\u0086\u0088\5\66\34\2\u0087\u0089\5> \2\u0088\u0087\3\2\2\2\u0088\u0089"+
		"\3\2\2\2\u0089\u008a\3\2\2\2\u008a\u008b\5T+\2\u008b\u008c\7n\2\2\u008c"+
		"\7\3\2\2\2\u008d\u00a4\5\16\b\2\u008e\u00a4\5\24\13\2\u008f\u00a4\5\22"+
		"\n\2\u0090\u00a4\5\26\f\2\u0091\u00a4\5\30\r\2\u0092\u00a4\5\36\20\2\u0093"+
		"\u00a4\5 \21\2\u0094\u00a4\5\f\7\2\u0095\u00a4\5$\23\2\u0096\u00a4\5,"+
		"\27\2\u0097\u00a4\5j\66\2\u0098\u00a4\5n8\2\u0099\u00a4\5P)\2\u009a\u00a4"+
		"\5\\/\2\u009b\u00a4\5N(\2\u009c\u00a4\5\60\31\2\u009d\u00a4\5r:\2\u009e"+
		"\u00a4\5t;\2\u009f\u00a4\5v<\2\u00a0\u00a4\5\62\32\2\u00a1\u00a4\5\64"+
		"\33\2\u00a2\u00a4\5\n\6\2\u00a3\u008d\3\2\2\2\u00a3\u008e\3\2\2\2\u00a3"+
		"\u008f\3\2\2\2\u00a3\u0090\3\2\2\2\u00a3\u0091\3\2\2\2\u00a3\u0092\3\2"+
		"\2\2\u00a3\u0093\3\2\2\2\u00a3\u0094\3\2\2\2\u00a3\u0095\3\2\2\2\u00a3"+
		"\u0096\3\2\2\2\u00a3\u0097\3\2\2\2\u00a3\u0098\3\2\2\2\u00a3\u0099\3\2"+
		"\2\2\u00a3\u009a\3\2\2\2\u00a3\u009b\3\2\2\2\u00a3\u009c\3\2\2\2\u00a3"+
		"\u009d\3\2\2\2\u00a3\u009e\3\2\2\2\u00a3\u009f\3\2\2\2\u00a3\u00a0\3\2"+
		"\2\2\u00a3\u00a1\3\2\2\2\u00a3\u00a2\3\2\2\2\u00a4\t\3\2\2\2\u00a5\u00a6"+
		"\5\66\34\2\u00a6\u00a7\7\4\2\2\u00a7\13\3\2\2\2\u00a8\u00ac\7\5\2\2\u00a9"+
		"\u00ad\5> \2\u00aa\u00ad\5\66\34\2\u00ab\u00ad\58\35\2\u00ac\u00a9\3\2"+
		"\2\2\u00ac\u00aa\3\2\2\2\u00ac\u00ab\3\2\2\2\u00ad\r\3\2\2\2\u00ae\u00ba"+
		"\t\2\2\2\u00af\u00b1\5\20\t\2\u00b0\u00af\3\2\2\2\u00b0\u00b1\3\2\2\2"+
		"\u00b1\u00bb\3\2\2\2\u00b2\u00b7\5\20\t\2\u00b3\u00b4\7\17\2\2\u00b4\u00b6"+
		"\5\20\t\2\u00b5\u00b3\3\2\2\2\u00b6\u00b9\3\2\2\2\u00b7\u00b5\3\2\2\2"+
		"\u00b7\u00b8\3\2\2\2\u00b8\u00bb\3\2\2\2\u00b9\u00b7\3\2\2\2\u00ba\u00b0"+
		"\3\2\2\2\u00ba\u00b2\3\2\2\2\u00bb\17\3\2\2\2\u00bc\u00c0\5F$\2\u00bd"+
		"\u00c0\5\66\34\2\u00be\u00c0\5> \2\u00bf\u00bc\3\2\2\2\u00bf\u00bd\3\2"+
		"\2\2\u00bf\u00be\3\2\2\2\u00c0\21\3\2\2\2\u00c1\u00c3\5\32\16\2\u00c2"+
		"\u00c4\5\34\17\2\u00c3\u00c2\3\2\2\2\u00c3\u00c4\3\2\2\2\u00c4\u00c5\3"+
		"\2\2\2\u00c5\u00c6\5\66\34\2\u00c6\23\3\2\2\2\u00c7\u00c9\5\32\16\2\u00c8"+
		"\u00ca\5\34\17\2\u00c9\u00c8\3\2\2\2\u00c9\u00ca\3\2\2\2\u00ca\u00cb\3"+
		"\2\2\2\u00cb\u00cc\5\66\34\2\u00cc\u00cd\7\20\2\2\u00cd\u00ce\5&\24\2"+
		"\u00ce\25\3\2\2\2\u00cf\u00d0\7\21\2\2\u00d0\u00d1\5\24\13\2\u00d1\27"+
		"\3\2\2\2\u00d2\u00d3\7\22\2\2\u00d3\u00d4\5\24\13\2\u00d4\31\3\2\2\2\u00d5"+
		"\u00d6\t\3\2\2\u00d6\33\3\2\2\2\u00d7\u00d8\7\32\2\2\u00d8\u00db\5&\24"+
		"\2\u00d9\u00da\7\17\2\2\u00da\u00dc\5&\24\2\u00db\u00d9\3\2\2\2\u00db"+
		"\u00dc\3\2\2\2\u00dc\u00dd\3\2\2\2\u00dd\u00de\7\33\2\2\u00de\35\3\2\2"+
		"\2\u00df\u00e0\5\"\22\2\u00e0\u00e1\7\20\2\2\u00e1\u00e2\5&\24\2\u00e2"+
		"\37\3\2\2\2\u00e3\u00e4\5\"\22\2\u00e4\u00e5\t\4\2\2\u00e5\u00e6\5&\24"+
		"\2\u00e6!\3\2\2\2\u00e7\u00ec\5:\36\2\u00e8\u00ec\5\66\34\2\u00e9\u00ec"+
		"\58\35\2\u00ea\u00ec\5(\25\2\u00eb\u00e7\3\2\2\2\u00eb\u00e8\3\2\2\2\u00eb"+
		"\u00e9\3\2\2\2\u00eb\u00ea\3\2\2\2\u00ec#\3\2\2\2\u00ed\u00ee\5\"\22\2"+
		"\u00ee\u00ef\t\5\2\2\u00ef%\3\2\2\2\u00f0\u00f1\b\24\1\2\u00f1\u00f2\7"+
		"\'\2\2\u00f2\u00f3\5&\24\2\u00f3\u00f4\7(\2\2\u00f4\u0100\3\2\2\2\u00f5"+
		"\u0100\5*\26\2\u00f6\u00f7\t\6\2\2\u00f7\u0100\5&\24\25\u00f8\u00f9\7"+
		">\2\2\u00f9\u0100\5&\24\b\u00fa\u0100\5L\'\2\u00fb\u0100\5:\36\2\u00fc"+
		"\u0100\5\66\34\2\u00fd\u0100\58\35\2\u00fe\u0100\5(\25\2\u00ff\u00f0\3"+
		"\2\2\2\u00ff\u00f5\3\2\2\2\u00ff\u00f6\3\2\2\2\u00ff\u00f8\3\2\2\2\u00ff"+
		"\u00fa\3\2\2\2\u00ff\u00fb\3\2\2\2\u00ff\u00fc\3\2\2\2\u00ff\u00fd\3\2"+
		"\2\2\u00ff\u00fe\3\2\2\2\u0100\u012b\3\2\2\2\u0101\u0102\f\24\2\2\u0102"+
		"\u0103\7+\2\2\u0103\u012a\5&\24\25\u0104\u0105\f\23\2\2\u0105\u0106\t"+
		"\7\2\2\u0106\u012a\5&\24\24\u0107\u0108\f\22\2\2\u0108\u0109\t\b\2\2\u0109"+
		"\u012a\5&\24\23\u010a\u010b\f\21\2\2\u010b\u010c\t\t\2\2\u010c\u012a\5"+
		"&\24\22\u010d\u010e\f\20\2\2\u010e\u010f\t\n\2\2\u010f\u012a\5&\24\21"+
		"\u0110\u0111\f\17\2\2\u0111\u0112\7\66\2\2\u0112\u012a\5&\24\20\u0113"+
		"\u0114\f\16\2\2\u0114\u0115\7\67\2\2\u0115\u012a\5&\24\17\u0116\u0117"+
		"\f\r\2\2\u0117\u0118\78\2\2\u0118\u012a\5&\24\16\u0119\u011a\f\13\2\2"+
		"\u011a\u011b\7;\2\2\u011b\u012a\5&\24\f\u011c\u011d\f\n\2\2\u011d\u011e"+
		"\7<\2\2\u011e\u012a\5&\24\13\u011f\u0120\f\t\2\2\u0120\u0121\7=\2\2\u0121"+
		"\u012a\5&\24\n\u0122\u0123\f\f\2\2\u0123\u0124\79\2\2\u0124\u0127\5&\24"+
		"\2\u0125\u0126\7:\2\2\u0126\u0128\5&\24\2\u0127\u0125\3\2\2\2\u0127\u0128"+
		"\3\2\2\2\u0128\u012a\3\2\2\2\u0129\u0101\3\2\2\2\u0129\u0104\3\2\2\2\u0129"+
		"\u0107\3\2\2\2\u0129\u010a\3\2\2\2\u0129\u010d\3\2\2\2\u0129\u0110\3\2"+
		"\2\2\u0129\u0113\3\2\2\2\u0129\u0116\3\2\2\2\u0129\u0119\3\2\2\2\u0129"+
		"\u011c\3\2\2\2\u0129\u011f\3\2\2\2\u0129\u0122\3\2\2\2\u012a\u012d\3\2"+
		"\2\2\u012b\u0129\3\2\2\2\u012b\u012c\3\2\2\2\u012c\'\3\2\2\2\u012d\u012b"+
		"\3\2\2\2\u012e\u0132\5\66\34\2\u012f\u0132\58\35\2\u0130\u0132\5:\36\2"+
		"\u0131\u012e\3\2\2\2\u0131\u012f\3\2\2\2\u0131\u0130\3\2\2\2\u0132\u0133"+
		"\3\2\2\2\u0133\u0134\5\34\17\2\u0134)\3\2\2\2\u0135\u0138\5\66\34\2\u0136"+
		"\u0138\58\35\2\u0137\u0135\3\2\2\2\u0137\u0136\3\2\2\2\u0138\u0139\3\2"+
		"\2\2\u0139\u013b\7\'\2\2\u013a\u013c\5.\30\2\u013b\u013a\3\2\2\2\u013b"+
		"\u013c\3\2\2\2\u013c\u013d\3\2\2\2\u013d\u013e\7(\2\2\u013e+\3\2\2\2\u013f"+
		"\u0142\5\66\34\2\u0140\u0142\58\35\2\u0141\u013f\3\2\2\2\u0141\u0140\3"+
		"\2\2\2\u0142\u0143\3\2\2\2\u0143\u0145\7\'\2\2\u0144\u0146\5.\30\2\u0145"+
		"\u0144\3\2\2\2\u0145\u0146\3\2\2\2\u0146\u0147\3\2\2\2\u0147\u0148\7("+
		"\2\2\u0148-\3\2\2\2\u0149\u014e\5&\24\2\u014a\u014b\7\17\2\2\u014b\u014d"+
		"\5&\24\2\u014c\u014a\3\2\2\2\u014d\u0150\3\2\2\2\u014e\u014c\3\2\2\2\u014e"+
		"\u014f\3\2\2\2\u014f/\3\2\2\2\u0150\u014e\3\2\2\2\u0151\u0153\7?\2\2\u0152"+
		"\u0154\5.\30\2\u0153\u0152\3\2\2\2\u0153\u0154\3\2\2\2\u0154\61\3\2\2"+
		"\2\u0155\u0156\7@\2\2\u0156\63\3\2\2\2\u0157\u0158\7A\2\2\u0158\65\3\2"+
		"\2\2\u0159\u015a\7o\2\2\u015a\67\3\2\2\2\u015b\u015e\7o\2\2\u015c\u015d"+
		"\7B\2\2\u015d\u015f\7o\2\2\u015e\u015c\3\2\2\2\u015f\u0160\3\2\2\2\u0160"+
		"\u015e\3\2\2\2\u0160\u0161\3\2\2\2\u01619\3\2\2\2\u0162\u0163\t\13\2\2"+
		"\u0163;\3\2\2\2\u0164\u0165\t\f\2\2\u0165=\3\2\2\2\u0166\u0168\t\r\2\2"+
		"\u0167\u0169\5@!\2\u0168\u0167\3\2\2\2\u0168\u0169\3\2\2\2\u0169?\3\2"+
		"\2\2\u016a\u016b\7M\2\2\u016bA\3\2\2\2\u016c\u016d\t\16\2\2\u016dC\3\2"+
		"\2\2\u016e\u016f\7\32\2\2\u016f\u0174\5&\24\2\u0170\u0171\7\17\2\2\u0171"+
		"\u0173\5&\24\2\u0172\u0170\3\2\2\2\u0173\u0176\3\2\2\2\u0174\u0172\3\2"+
		"\2\2\u0174\u0175\3\2\2\2\u0175\u0177\3\2\2\2\u0176\u0174\3\2\2\2\u0177"+
		"\u0178\7\33\2\2\u0178E\3\2\2\2\u0179\u017a\7t\2\2\u017aG\3\2\2\2\u017b"+
		"\u017c\7v\2\2\u017cI\3\2\2\2\u017d\u017e\7s\2\2\u017eK\3\2\2\2\u017f\u0186"+
		"\5> \2\u0180\u0186\5B\"\2\u0181\u0186\5D#\2\u0182\u0186\5F$\2\u0183\u0186"+
		"\5H%\2\u0184\u0186\5J&\2\u0185\u017f\3\2\2\2\u0185\u0180\3\2\2\2\u0185"+
		"\u0181\3\2\2\2\u0185\u0182\3\2\2\2\u0185\u0183\3\2\2\2\u0185\u0184\3\2"+
		"\2\2\u0186M\3\2\2\2\u0187\u0188\7P\2\2\u0188\u0189\7u\2\2\u0189O\3\2\2"+
		"\2\u018a\u018b\7Q\2\2\u018b\u018c\5\66\34\2\u018c\u018e\7\'\2\2\u018d"+
		"\u018f\5V,\2\u018e\u018d\3\2\2\2\u018e\u018f\3\2\2\2\u018f\u0190\3\2\2"+
		"\2\u0190\u0192\7(\2\2\u0191\u0193\5R*\2\u0192\u0191\3\2\2\2\u0192\u0193"+
		"\3\2\2\2\u0193\u0194\3\2\2\2\u0194\u0195\5T+\2\u0195\u0196\7n\2\2\u0196"+
		"Q\3\2\2\2\u0197\u0198\7R\2\2\u0198\u0199\5Z.\2\u0199S\3\2\2\2\u019a\u019b"+
		"\7S\2\2\u019b\u01a0\7n\2\2\u019c\u019f\5\b\5\2\u019d\u019f\7n\2\2\u019e"+
		"\u019c\3\2\2\2\u019e\u019d\3\2\2\2\u019f\u01a2\3\2\2\2\u01a0\u019e\3\2"+
		"\2\2\u01a0\u01a1\3\2\2\2\u01a1\u01a3\3\2\2\2\u01a2\u01a0\3\2\2\2\u01a3"+
		"\u01a4\7T\2\2\u01a4U\3\2\2\2\u01a5\u01aa\5X-\2\u01a6\u01a7\7\17\2\2\u01a7"+
		"\u01a9\5X-\2\u01a8\u01a6\3\2\2\2\u01a9\u01ac\3\2\2\2\u01aa\u01a8\3\2\2"+
		"\2\u01aa\u01ab\3\2\2\2\u01abW\3\2\2\2\u01ac\u01aa\3\2\2\2\u01ad\u01ae"+
		"\5\66\34\2\u01ae\u01af\7\4\2\2\u01af\u01b0\5\32\16\2\u01b0Y\3\2\2\2\u01b1"+
		"\u01b6\5\32\16\2\u01b2\u01b3\7\17\2\2\u01b3\u01b5\5\32\16\2\u01b4\u01b2"+
		"\3\2\2\2\u01b5\u01b8\3\2\2\2\u01b6\u01b4\3\2\2\2\u01b6\u01b7\3\2\2\2\u01b7"+
		"[\3\2\2\2\u01b8\u01b6\3\2\2\2\u01b9\u01ba\7U\2\2\u01ba\u01bb\5\66\34\2"+
		"\u01bb\u01bd\7\'\2\2\u01bc\u01be\5`\61\2\u01bd\u01bc\3\2\2\2\u01bd\u01be"+
		"\3\2\2\2\u01be\u01bf\3\2\2\2\u01bf\u01c0\7(\2\2\u01c0\u01c1\7R\2\2\u01c1"+
		"\u01c2\7V\2\2\u01c2\u01c4\7\'\2\2\u01c3\u01c5\5d\63\2\u01c4\u01c3\3\2"+
		"\2\2\u01c4\u01c5\3\2\2\2\u01c5\u01c6\3\2\2\2\u01c6\u01c7\7(\2\2\u01c7"+
		"\u01c8\7R\2\2\u01c8\u01ca\7\'\2\2\u01c9\u01cb\5f\64\2\u01ca\u01c9\3\2"+
		"\2\2\u01ca\u01cb\3\2\2\2\u01cb\u01cc\3\2\2\2\u01cc\u01cf\7(\2\2\u01cd"+
		"\u01d0\5^\60\2\u01ce\u01d0\5T+\2\u01cf\u01cd\3\2\2\2\u01cf\u01ce\3\2\2"+
		"\2\u01d0]\3\2\2\2\u01d1\u01d2\7\20\2\2\u01d2\u01d3\5> \2\u01d3_\3\2\2"+
		"\2\u01d4\u01d9\5b\62\2\u01d5\u01d6\7\17\2\2\u01d6\u01d8\5b\62\2\u01d7"+
		"\u01d5\3\2\2\2\u01d8\u01db\3\2\2\2\u01d9\u01d7\3\2\2\2\u01d9\u01da\3\2"+
		"\2\2\u01daa\3\2\2\2\u01db\u01d9\3\2\2\2\u01dc\u01dd\5\66\34\2\u01dd\u01de"+
		"\7\4\2\2\u01de\u01df\5\32\16\2\u01df\u01e2\7W\2\2\u01e0\u01e3\5:\36\2"+
		"\u01e1\u01e3\5<\37\2\u01e2\u01e0\3\2\2\2\u01e2\u01e1\3\2\2\2\u01e3c\3"+
		"\2\2\2\u01e4\u01e9\5:\36\2\u01e5\u01e6\7\17\2\2\u01e6\u01e8\5:\36\2\u01e7"+
		"\u01e5\3\2\2\2\u01e8\u01eb\3\2\2\2\u01e9\u01e7\3\2\2\2\u01e9\u01ea\3\2"+
		"\2\2\u01eae\3\2\2\2\u01eb\u01e9\3\2\2\2\u01ec\u01f1\5h\65\2\u01ed\u01ee"+
		"\7\17\2\2\u01ee\u01f0\5h\65\2\u01ef\u01ed\3\2\2\2\u01f0\u01f3\3\2\2\2"+
		"\u01f1\u01ef\3\2\2\2\u01f1\u01f2\3\2\2\2\u01f2g\3\2\2\2\u01f3\u01f1\3"+
		"\2\2\2\u01f4\u01f5\5\32\16\2\u01f5\u01f8\7W\2\2\u01f6\u01f9\5:\36\2\u01f7"+
		"\u01f9\5<\37\2\u01f8\u01f6\3\2\2\2\u01f8\u01f7\3\2\2\2\u01f9i\3\2\2\2"+
		"\u01fa\u01fb\7X\2\2\u01fb\u01fd\5&\24\2\u01fc\u01fe\7n\2\2\u01fd\u01fc"+
		"\3\2\2\2\u01fd\u01fe\3\2\2\2\u01fe\u0201\3\2\2\2\u01ff\u0202\5\b\5\2\u0200"+
		"\u0202\5T+\2\u0201\u01ff\3\2\2\2\u0201\u0200\3\2\2\2\u0202\u0204\3\2\2"+
		"\2\u0203\u0205\7n\2\2\u0204\u0203\3\2\2\2\u0204\u0205\3\2\2\2\u0205\u0207"+
		"\3\2\2\2\u0206\u0208\5l\67\2\u0207\u0206\3\2\2\2\u0207\u0208\3\2\2\2\u0208"+
		"\u0209\3\2\2\2\u0209\u020a\7n\2\2\u020ak\3\2\2\2\u020b\u020d\7Y\2\2\u020c"+
		"\u020e\7n\2\2\u020d\u020c\3\2\2\2\u020d\u020e\3\2\2\2\u020e\u0211\3\2"+
		"\2\2\u020f\u0212\5\b\5\2\u0210\u0212\5T+\2\u0211\u020f\3\2\2\2\u0211\u0210"+
		"\3\2\2\2\u0212m\3\2\2\2\u0213\u0215\5p9\2\u0214\u0216\7n\2\2\u0215\u0214"+
		"\3\2\2\2\u0215\u0216\3\2\2\2\u0216\u0219\3\2\2\2\u0217\u021a\5\b\5\2\u0218"+
		"\u021a\5T+\2\u0219\u0217\3\2\2\2\u0219\u0218\3\2\2\2\u021a\u021c\3\2\2"+
		"\2\u021b\u021d\7n\2\2\u021c\u021b\3\2\2\2\u021c\u021d\3\2\2\2\u021d\u021f"+
		"\3\2\2\2\u021e\u0220\5l\67\2\u021f\u021e\3\2\2\2\u021f\u0220\3\2\2\2\u0220"+
		"\u0221\3\2\2\2\u0221\u0222\7n\2\2\u0222o\3\2\2\2\u0223\u0224\t\17\2\2"+
		"\u0224q\3\2\2\2\u0225\u0228\7f\2\2\u0226\u0229\5:\36\2\u0227\u0229\5\66"+
		"\34\2\u0228\u0226\3\2\2\2\u0228\u0227\3\2\2\2\u0229\u022a\3\2\2\2\u022a"+
		"\u022b\7g\2\2\u022b\u022d\5&\24\2\u022c\u022e\7n\2\2\u022d\u022c\3\2\2"+
		"\2\u022d\u022e\3\2\2\2\u022e\u022f\3\2\2\2\u022f\u0230\5T+\2\u0230s\3"+
		"\2\2\2\u0231\u0232\7h\2\2\u0232\u0234\5&\24\2\u0233\u0235\7n\2\2\u0234"+
		"\u0233\3\2\2\2\u0234\u0235\3\2\2\2\u0235\u0238\3\2\2\2\u0236\u0239\5\b"+
		"\5\2\u0237\u0239\5T+\2\u0238\u0236\3\2\2\2\u0238\u0237\3\2\2\2\u0239u"+
		"\3\2\2\2\u023a\u023d\7i\2\2\u023b\u023e\5\b\5\2\u023c\u023e\5T+\2\u023d"+
		"\u023b\3\2\2\2\u023d\u023c\3\2\2\2\u023e\u0240\3\2\2\2\u023f\u0241\7n"+
		"\2\2\u0240\u023f\3\2\2\2\u0240\u0241\3\2\2\2\u0241\u0242\3\2\2\2\u0242"+
		"\u0243\7j\2\2\u0243\u0244\5&\24\2\u0244w\3\2\2\2>z|\u0083\u0088\u00a3"+
		"\u00ac\u00b0\u00b7\u00ba\u00bf\u00c3\u00c9\u00db\u00eb\u00ff\u0127\u0129"+
		"\u012b\u0131\u0137\u013b\u0141\u0145\u014e\u0153\u0160\u0168\u0174\u0185"+
		"\u018e\u0192\u019e\u01a0\u01aa\u01b6\u01bd\u01c4\u01ca\u01cf\u01d9\u01e2"+
		"\u01e9\u01f1\u01f8\u01fd\u0201\u0204\u0207\u020d\u0211\u0215\u0219\u021c"+
		"\u021f\u0228\u022d\u0234\u0238\u023d\u0240";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}