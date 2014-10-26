package com.cogitareforma.makhana.common.util;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.codehaus.groovy.ast.stmt.SynchronizedStatement;
import org.codehaus.groovy.classgen.BytecodeExpression;
import org.codehaus.groovy.classgen.BytecodeSequence;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.codehaus.groovy.control.customizers.SecureASTCustomizer;
import org.codehaus.groovy.syntax.Types;

import com.google.common.collect.ImmutableList;

public class ScriptManager
{
	private static final String[ ] defaultClassConstructionWhitelist = new String[ ]
	{
			Math.class.getName( ), Date.class.getName( ), Map.class.getName( ), List.class.getName( ), Set.class.getName( ),
			ArrayList.class.getName( ), Arrays.class.getName( ), HashMap.class.getName( ), HashSet.class.getName( ),
			BigDecimal.class.getName( ), String.class.getName( ), Boolean.class.getName( ), Boolean.TYPE.getName( )
	};

	private static final String[ ] defaultReceiverWhitelist = new String[ ]
	{
			Math.class.getName( ), Integer.class.getName( ), Float.class.getName( ), Double.class.getName( ), Long.class.getName( ),
			Short.class.getName( ), Character.class.getName( ), Byte.class.getName( ), Boolean.class.getName( ),
			BigDecimal.class.getName( ), Arrays.class.getName( ), Date.class.getName( ), List.class.getName( ), Map.class.getName( ),
			Set.class.getName( ), Object.class.getName( ), Integer.TYPE.getName( ), Long.TYPE.getName( ), Float.TYPE.getName( ),
			Double.TYPE.getName( ), String.class.getName( ), Boolean.TYPE.getName( )
	};

	private static final Integer[ ] defaultTokensWhiteList = new Integer[ ]
	{
			Types.PLUS, Types.MINUS, Types.MULTIPLY, Types.DIVIDE, Types.MOD, Types.POWER, Types.PLUS_PLUS, Types.MINUS_MINUS,
			Types.COMPARE_EQUAL, Types.COMPARE_NOT_EQUAL, Types.COMPARE_LESS_THAN, Types.COMPARE_LESS_THAN_EQUAL,
			Types.COMPARE_GREATER_THAN, Types.COMPARE_GREATER_THAN_EQUAL, Types.ASSIGNMENT_OPERATOR, Types.ASSIGN
	};

	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( ScriptManager.class.getName( ) );

	private Binding binding;
	private ImmutableList< String > constructionWhiteList;
	private ImportCustomizer imports;
	private ImmutableList< String > receiverWhiteList;
	private SecureASTCustomizer secure;
	private GroovyShell shell;
	private ImmutableList< Integer > tokenWhiteList;

	public ScriptManager( )
	{
		constructionWhiteList = ImmutableList.copyOf( defaultClassConstructionWhitelist );
		receiverWhiteList = ImmutableList.copyOf( defaultReceiverWhitelist );
		tokenWhiteList = ImmutableList.copyOf( defaultTokensWhiteList );

		imports = new ImportCustomizer( ).addStaticStars( Math.class.getName( ) );
		secure = new SecureASTCustomizer( );
		secure.setPackageAllowed( true );
		secure.setClosuresAllowed( true );
		secure.setMethodDefinitionAllowed( true );

		secure.setImportsWhitelist( constructionWhiteList );
		secure.setConstantTypesWhiteList( receiverWhiteList );
		secure.setReceiversWhiteList( receiverWhiteList );
		secure.setTokensWhitelist( tokenWhiteList );

		// Only remove the dangerous Expressions
		secure.setExpressionsBlacklist( Arrays.asList( BytecodeExpression.class ) );

		// Only remove the dangerous Statements
		secure.setStatementsBlacklist( Arrays.asList( BytecodeSequence.class, SynchronizedStatement.class ) );

		CompilerConfiguration compilerConfig = new CompilerConfiguration( );
		compilerConfig.addCompilationCustomizers( imports, secure );

		binding = new Binding( );
		binding.setVariable( "foo", new Integer( 2 ) );

		shell = new GroovyShell( binding, compilerConfig );

		// TODO: Look into replacing Shell with GroovyScriptEngine
		try
		{
			shell.run( new File( "test.groovy" ), Collections.emptyList( ) );
		}
		catch ( CompilationFailedException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace( );
		}
		catch ( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace( );
		}
	}

	public Object evaluateScript( String script )
	{
		Object eval = null;
		try
		{
			eval = shell.evaluate( script );
		}
		catch ( Exception e )
		{
			logger.log( Level.SEVERE, "An error occured evaluating a script: " + e.getMessage( ) );
		}
		return eval;
	}

	public boolean executeScript( String script )
	{
		try
		{
			shell.evaluate( script );
		}
		catch ( Exception e )
		{
			logger.log( Level.SEVERE, "An error occured executing a script: " + e.getMessage( ) );
			return false;
		}
		return true;
	}

	public Object getVariable( String name )
	{
		return binding.getVariable( name );
	}

	public Map< ?, ? > getVariables( )
	{
		return binding.getVariables( );
	}

	public void setVariable( String name, Object value )
	{
		if ( name == null )
		{
			throw new NullPointerException( "Variable name can not be null" );
		}
		binding.setVariable( name, value );
	}
}
