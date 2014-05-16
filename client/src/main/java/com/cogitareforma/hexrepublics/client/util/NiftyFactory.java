package com.cogitareforma.hexrepublics.client.util;

import com.cogitareforma.hexrepublics.client.DebugGlobals;
import com.cogitareforma.hexrepublics.client.views.ConsoleViewController;
import com.cogitareforma.hexrepublics.client.views.HudViewController;
import com.cogitareforma.hexrepublics.client.views.LoadingViewController;
import com.cogitareforma.hexrepublics.client.views.LobbyViewController;
import com.cogitareforma.hexrepublics.client.views.NetworkViewController;
import com.cogitareforma.hexrepublics.client.views.OptionsViewController;
import com.cogitareforma.hexrepublics.client.views.StartViewController;
import com.cogitareforma.hexrepublics.client.views.TileViewController;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ControlDefinitionBuilder;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.PopupBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.chatcontrol.builder.ChatBuilder;
import de.lessvoid.nifty.controls.checkbox.builder.CheckboxBuilder;
import de.lessvoid.nifty.controls.console.builder.ConsoleBuilder;
import de.lessvoid.nifty.controls.dropdown.builder.DropDownBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.controls.listbox.builder.ListBoxBuilder;
import de.lessvoid.nifty.controls.radiobutton.builder.RadioButtonBuilder;
import de.lessvoid.nifty.controls.radiobutton.builder.RadioGroupBuilder;
import de.lessvoid.nifty.controls.slider.builder.SliderBuilder;
import de.lessvoid.nifty.controls.textfield.builder.TextFieldBuilder;

/**
 * 
 * @author Ryan Grier
 * @author Elliott Butler
 * 
 */
public class NiftyFactory
{
	public static void createLoadingScreen( Nifty nifty )
	{
		new ControlDefinitionBuilder( "bar" )
		{
			{
				controller( new LoadingViewController( ) );
				image( new ImageBuilder( )
				{
					{
						filename( "Interface/border.png" );
						imageMode( "resize:15,2,15,15,15,2,15,2,15,2,15,15" );
						childLayoutAbsolute( );
						image( new ImageBuilder( "inner" )
						{
							{
								x( "0" );
								y( "0" );
								filename( "Interface/inner.png" );
								width( "32px" );
								height( "100%" );
								imageMode( "resize:15,2,15,15,15,2,15,2,15,2,15,15" );
							}
						} );
					}
				} );
			}
		}.registerControlDefintion( nifty );

		nifty.addScreen( "loading", new ScreenBuilder( "loading" )
		{
			{
				controller( new LoadingViewController( ) );
				layer( new LayerBuilder( "layer" )
				{
					{
						childLayoutCenter( );
						backgroundColor( "#303030ff" );

						panel( new PanelBuilder( "loadingBar" )
						{
							{
								height( "100%" );
								width( "100%" );
								childLayoutCenter( );
								control( new ControlBuilder( "bar", "bar" )
								{
									{
										name( "bar" );
										alignCenter( );
										valignBottom( );
										width( "80%" );
										height( "5%" );
									}
								} );
								control( new LabelBuilder( )
								{
									{
										name( "label" );
										alignCenter( );
										text( "" );
									}
								} );
							}
						} );
					}
				} );
			}
		}.build( nifty ) );
	}

	@SuppressWarnings( "unused" )
	public static void createGameLobby( Nifty nifty )
	{
		nifty.addScreen( "lobby", new ScreenBuilder( "lobby" )
		{

			{
				controller( new LobbyViewController( ) );

				layer( new LayerBuilder( "layer" )
				{

					{
						childLayoutVertical( );
						backgroundColor( "#838796ff" );

						panel( new PanelBuilder( "lobbyHeader" )
						{
							{
								width( "100%" );
								height( "32px" );
								valignBottom( );
								childLayoutHorizontal( );
								backgroundColor( "#616374ff" );
								paddingLeft( "32px" );
								paddingRight( "32px" );

								text( new TextBuilder( "lobbyTitle" )
								{
									{
										width( "50%" );
										height( "100%" );
										text( "Game Lobby" );
										textHAlignLeft( );
										textVAlignCenter( );
										font( "Interface/Fonts/Default.fnt" );

									}
								} );

								panel( new PanelBuilder( "lobbyButtons" )
								{
									{
										width( "50%" );
										childLayoutHorizontal( );

										panel( new PanelBuilder( "spacer" )
										{
											{
												width( "*" );
											}
										} );

										control( new ButtonBuilder( "leave", "Leave" )
										{

											{
												alignCenter( );
												valignCenter( );
												visibleToMouse( true );
												interactOnClick( "back()" );
											}
										} );

									}
								} );

							}
						} );

						panel( new PanelBuilder( "mainPanel" )
						{

							{
								width( "66%" );
								height( "100%" );
								padding( "32px" );
								alignCenter( );
								valignCenter( );
								childLayoutVertical( );

								panel( new PanelBuilder( "playerList" )
								{
									{
										style( "nifty-panel-brown" );
										childLayoutVertical( );

										control( new LabelBuilder( "labelPlayers", "Players" )
										{
											{
												textHAlignLeft( );
												textVAlignBottom( );
												width( "100%" );
											}
										} );

										panel( new PanelBuilder( "players" )
										{
											{
												childLayoutHorizontal( );
												style( "nifty-panel-inset-beige" );
												width( "100%" );

												panel( new PanelBuilder( "player1" )
												{
													{
														backgroundColor( "#404040ff" );
														width( "25%" );
														childLayoutVertical( );

														control( new LabelBuilder( "player1name" )
														{
															{
																if ( DebugGlobals.EASTEREGGFONT == true )
																{
																	font( "Interface/Fonts/AngerthasMoria.fnt" );
																}
																alignCenter( );
																valignBottom( );
																width( "100%" );
																wrap( true );
																color( "#f2f2f2ff" );
															}
														} );
													}
												} );
												panel( new PanelBuilder( "player2" )
												{
													{
														backgroundColor( "#303030ff" );
														width( "25%" );
														childLayoutVertical( );

														control( new LabelBuilder( "player2name" )
														{
															{
																if ( DebugGlobals.EASTEREGGFONT == true )
																{
																	font( "Interface/Fonts/AngerthasMoria.fnt" );
																}
																alignCenter( );
																valignBottom( );
																width( "100%" );
																wrap( true );
																color( "#f2f2f2ff" );
															}
														} );
													}
												} );
												panel( new PanelBuilder( "player3" )
												{
													{
														backgroundColor( "#404040ff" );
														width( "25%" );
														childLayoutVertical( );

														control( new LabelBuilder( "player3name" )
														{
															{
																if ( DebugGlobals.EASTEREGGFONT == true )
																{
																	font( "Interface/Fonts/AngerthasMoria.fnt" );
																}
																alignCenter( );
																valignBottom( );
																width( "100%" );
																wrap( true );
																color( "#f2f2f2ff" );
															}
														} );
													}
												} );
												panel( new PanelBuilder( "player4" )
												{
													{
														backgroundColor( "#303030ff" );
														width( "25%" );
														childLayoutVertical( );

														control( new LabelBuilder( "player4name" )
														{
															{
																if ( DebugGlobals.EASTEREGGFONT == true )
																{
																	font( "Interface/Fonts/AngerthasMoria.fnt" );
																}
																alignCenter( );
																valignBottom( );
																width( "100%" );
																wrap( true );
																color( "#f2f2f2ff" );
															}
														} );
													}
												} );
											}
										} );

										control( new LabelBuilder( "labelOptions", "Options" )
										{
											{
												textHAlignLeft( );
												textVAlignBottom( );
												width( "100%" );
											}
										} );

										panel( new PanelBuilder( "lobbyButtons" )
										{
											{
												childLayoutHorizontal( );
												style( "nifty-panel-inset-beige" );
												width( "100%" );

												control( new ButtonBuilder( "readyup", "Ready Up" )
												{

													{
														alignCenter( );
														valignCenter( );
														visibleToMouse( true );
														interactOnClick( "readyUp()" );
													}
												} );

											}
										} );

									}
								} );

								control( new ChatBuilder( "lobbyChat", 8 )
								{
									{
										sendLabel( "Send Message" );
									}
								} );

							}
						} );
					}
				} );
			}
		}.build( nifty ) );
	}

	/**
	 * Builds and adds the start screen with a login popup.
	 * 
	 * @param nifty
	 */
	public static void createStartView( Nifty nifty )
	{
		nifty.addScreen( "start", new ScreenBuilder( "start" )
		{
			{
				controller( new StartViewController( ) );

				layer( new LayerBuilder( "layer" )
				{

					{
						childLayoutCenter( );
						backgroundColor( "#1a1a1aff" );

						panel( new PanelBuilder( "startPanel" )
						{

							{
								width( "25%" );
								height( "100%" );
								childLayoutVertical( );

								panel( new PanelBuilder( "" )
								{

									{

										height( "40%" );
										childLayoutCenter( );
										alignCenter( );

										text( new TextBuilder( )
										{
											{
												text( "Hex Republics" );
												font( "Interface/Fonts/Default.fnt" );
												height( "100%" );
												width( "100%" );
											}
										} );
									}
								} );

								panel( new PanelBuilder( "startButtonsPanel" )
								{

									{
										style( "nifty-panel-beige" );
										alignCenter( );
										childLayoutVertical( );

										control( new ButtonBuilder( "startSingle", "Singleplayer" )
										{

											{
												alignCenter( );
												visibleToMouse( true );
											}
										} );

										control( new ButtonBuilder( "startLogin", "Multiplayer" )
										{

											{
												alignCenter( );
												visibleToMouse( true );
												interactOnClick( "openLogin()" );
											}
										} );

										control( new ButtonBuilder( "startSettings", "Settings" )
										{
											{
												alignCenter( );
												visibleToMouse( true );
												interactOnClick( "openOptions()" );
											}
										} );

										control( new ButtonBuilder( "startExit", "Exit" )
										{

											{
												alignCenter( );
												visibleToMouse( true );
												interactOnClick( "quit()" );
											}
										} );
									}
								} );
							}
						} );
					}
				} );
			}
		}.build( nifty ) );
		new PopupBuilder( "loginPopup" )
		{
			{
				backgroundColor( "#0f0f0fff" );
				childLayoutCenter( );
				panel( new PanelBuilder( "loginPanel" )
				{
					{
						style( "nifty-panel-brown" );
						childLayoutVertical( );
						width( "320px" );
						alignCenter( );
						valignCenter( );

						panel( new PanelBuilder( "loginInputs" )
						{
							{
								style( "nifty-panel-inset-beige" );
								childLayoutVertical( );
								panel( new PanelBuilder( "loginUsername" )
								{
									{
										// backgroundColor( "#404040ff" );
										childLayoutHorizontal( );
										height( "32px" );

										control( new LabelBuilder( "labelUsername", "Username: " )
										{
											{
												// textHAlignRight( );
												width( "25%" );
											}
										} );
										control( new TextFieldBuilder( "username", "" )
										{
											{
												alignCenter( );
												maxLength( 32 );
												width( "75%" );
											}
										} );
									}
								} );

								panel( new PanelBuilder( "loginPassword" )
								{
									{
										// backgroundColor( "#303030ff" );
										childLayoutHorizontal( );
										height( "32px" );

										control( new LabelBuilder( "labelPassword", "Password: " )
										{
											{
												// textHAlignRight( );
												width( "25%" );
											}
										} );
										control( new TextFieldBuilder( "password", "" )
										{
											{
												alignCenter( );
												maxLength( 32 );
												width( "75%" );
											}
										} );
									}
								} );
							}
						} );

						control( new LabelBuilder( "loginFail" )
						{
							{
								alignCenter( );
								valignBottom( );
								height( "32px" );
								width( "100%" );
								wrap( true );
								// color( "#5c3566ff" );
								color( "#d2b290ff" );
							}
						} );

						panel( new PanelBuilder( "loginButtons" )
						{
							{
								backgroundColor( "#404040ff" );
								childLayoutHorizontal( );
								textHAlignCenter( );
								alignCenter( );

								control( new ButtonBuilder( "login", "Login" )
								{
									{
										alignCenter( );
										valignBottom( );
										visibleToMouse( true );
										interactOnClick( "startGame()" );
									}
								} );
								control( new ButtonBuilder( "back", "Back" )
								{
									{
										alignCenter( );
										valignBottom( );
										visibleToMouse( true );
										interactOnClick( "closeLogin()" );
									}
								} );
							}
						} );

					}
				} );
			}
		}.registerPopup( nifty );
	}

	/**
	 * Builds and adds main options with graphics control.
	 * 
	 * @param nifty
	 */
	public static void createMainOptions( Nifty nifty )
	{
		nifty.addScreen( "mainOptions", new ScreenBuilder( "mainOptions" )
		{
			{
				controller( new OptionsViewController( ) );

				layer( new LayerBuilder( "layer" )
				{
					{
						backgroundColor( "#838796ff" );
						childLayoutVertical( );

						panel( new PanelBuilder( "optionsHeader" )
						{
							{
								width( "100%" );
								height( "32px" );
								valignBottom( );
								childLayoutHorizontal( );
								backgroundColor( "#616374ff" );
								paddingLeft( "32px" );
								paddingRight( "32px" );

								text( new TextBuilder( "optionsTitle" )
								{
									{
										width( "50%" );
										height( "100%" );
										text( "Options" );
										textHAlignLeft( );
										textVAlignCenter( );
										font( "Interface/Fonts/Default.fnt" );

									}
								} );

								panel( new PanelBuilder( "optionsButtons" )
								{
									{
										width( "50%" );
										childLayoutHorizontal( );

										panel( new PanelBuilder( "spacer" )
										{
											{
												width( "*" );
											}
										} );
										control( new ButtonBuilder( "selectGraphics", "Apply" )
										{
											{
												alignCenter( );
												valignCenter( );
												visibleToMouse( true );
												interactOnClick( "applySettings()" );
											}
										} );
										control( new ButtonBuilder( "backtostart", "Back" )
										{
											{
												alignCenter( );
												valignCenter( );
												visibleToMouse( true );
												interactOnClick( "exitMainOptions()" );
											}
										} );
									}
								} );

							}
						} );
						panel( new PanelBuilder( "optionsTabs" )
						{
							{
								width( "66%" );
								height( "100%" );
								padding( "32px" );
								alignCenter( );
								valignCenter( );
								childLayoutVertical( );

								panel( new PanelBuilder( "graphics" )
								{
									{
										style( "nifty-panel-brown" );
										childLayoutVertical( );

										control( new LabelBuilder( "labelGraphics", "Graphics" )
										{
											{
												textHAlignLeft( );
												textVAlignBottom( );
												width( "100%" );
											}
										} );

										panel( new PanelBuilder( "graphicsSettings" )
										{
											{
												style( "nifty-panel-inset-beige" );
												childLayoutVertical( );

												panel( new PanelBuilder( "optionsResolution" )
												{
													{
														childLayoutHorizontal( );
														control( new LabelBuilder( "labelResolution", "Screen Resolution: " )
														{
															{
																textHAlignLeft( );
																width( "25%" );
															}
														} );
														control( new DropDownBuilder( "graphicsOptions" )
														{
															{

															}
														} );
													}
												} );
												panel( new PanelBuilder( "optionsFullscreen" )
												{
													{
														childLayoutHorizontal( );

														control( new LabelBuilder( "labelFullscreen", "Fullscreen: " )
														{
															{
																textHAlignLeft( );
																width( "25%" );
															}
														} );
														control( new CheckboxBuilder( "graphicsFullscreen" )
														{
															{

															}
														} );
													}
												} );
												panel( new PanelBuilder( "optionsQuality" )
												{
													{
														childLayoutHorizontal( );

														control( new LabelBuilder( "labelQuality", "Quality: " )
														{
															{
																textHAlignLeft( );
																width( "25%" );
															}
														} );

														control( new SliderBuilder( "sliderA", false )
														{
															{
																buttonStepSize( 1.0f );
																max( 2.0f );
															}
														} );

													}
												} );
												panel( new PanelBuilder( "optionsVSync" )
												{
													{
														childLayoutHorizontal( );

														control( new LabelBuilder( "labelVSync", "VSync: " )
														{
															{
																textHAlignLeft( );
																width( "25%" );
															}
														} );
														control( new CheckboxBuilder( "graphicsVSync" )
														{
															{

															}
														} );
													}
												} );
											}
										} );
									}
								} );

								panel( new PanelBuilder( "audio" )
								{
									{
										style( "nifty-panel-brown" );
										childLayoutVertical( );

										control( new LabelBuilder( "audioOptions", "Audio" )
										{
											{
												textHAlignLeft( );
												textVAlignBottom( );
												width( "100%" );
											}
										} );

										panel( new PanelBuilder( "audioSettings" )
										{
											{
												style( "nifty-panel-inset-beige" );
												childLayoutVertical( );
												panel( new PanelBuilder( "optionsMainAudio" )
												{
													{

														childLayoutHorizontal( );
														control( new LabelBuilder( "labelMainVolume", "Main Volume: " )
														{
															{
																textHAlignLeft( );
																width( "25%" );
															}
														} );

														control( new SliderBuilder( "mainVolumeSlider", false )
														{
															{
																buttonStepSize( 5.0f );
																max( 100.0f );
															}
														} );
													}
												} );
												panel( new PanelBuilder( "optionsMusic" )
												{
													{

														childLayoutHorizontal( );
														control( new LabelBuilder( "labelMusic", "Music Volume: " )
														{
															{
																textHAlignLeft( );
																width( "25%" );
															}
														} );

														control( new SliderBuilder( "musicSlider", false )
														{
															{
																buttonStepSize( 5.0f );
																max( 100.0f );
															}
														} );
													}
												} );
												panel( new PanelBuilder( "optionsSounds" )
												{
													{

														childLayoutHorizontal( );
														control( new LabelBuilder( "labelSounds", "Sounds Volume: " )
														{
															{
																textHAlignLeft( );
																width( "25%" );
															}
														} );

														control( new SliderBuilder( "soundsSlider", false )
														{
															{
																buttonStepSize( 5.0f );
																max( 100.0f );
															}
														} );
													}
												} );
											}
										} );
									}
								} );
								panel( new PanelBuilder( "input" )
								{
									{
										style( "nifty-panel-brown" );
										childLayoutVertical( );
										control( new LabelBuilder( "inputOptions", "Input" )
										{
											{
												textHAlignLeft( );
												textVAlignBottom( );
												width( "100%" );
											}
										} );

										panel( new PanelBuilder( "inputSettings" )
										{
											{
												style( "nifty-panel-inset-beige" );
												childLayoutVertical( );

												panel( new PanelBuilder( "consoleCheck" )
												{
													{

														childLayoutHorizontal( );
														control( new LabelBuilder( "consoleLabel", "Enable Dev Console: " )
														{
															{
																textHAlignLeft( );
																width( "25%" );
															}
														} );
														control( new CheckboxBuilder( "consoleCheckBox" )
														{
															{
																checked( false );
															}
														} );
													}
												} );
												panel( new PanelBuilder( "inputNorth" )
												{
													{

														childLayoutHorizontal( );
														control( new LabelBuilder( "northLabel", "Move North: " )
														{
															{
																textHAlignLeft( );
																width( "25%" );
															}
														} );
														control( new ButtonBuilder( "northButton", "" )
														{
															{
																visibleToMouse( true );
																interactOnClick( "setKeyBinding(northButton)" );
															}
														} );
													}
												} );

												panel( new PanelBuilder( "inputSouth" )
												{
													{

														childLayoutHorizontal( );
														control( new LabelBuilder( "southLabel", "Move South: " )
														{
															{
																textHAlignLeft( );
																width( "25%" );
															}
														} );
														control( new ButtonBuilder( "southButton", "" )
														{
															{
																visibleToMouse( true );
																interactOnClick( "setKeyBinding(southButton)" );
															}
														} );
													}
												} );

												panel( new PanelBuilder( "inputEast" )
												{
													{

														childLayoutHorizontal( );
														control( new LabelBuilder( "eastLabel", "Move East: " )
														{
															{
																textHAlignLeft( );
																width( "25%" );
															}
														} );
														control( new ButtonBuilder( "eastButton", "" )
														{
															{
																visibleToMouse( true );
																interactOnClick( "setKeyBinding(eastButton)" );
															}
														} );
													}
												} );

												panel( new PanelBuilder( "inputWest" )
												{
													{

														childLayoutHorizontal( );
														control( new LabelBuilder( "westLabel", "Move West: " )
														{
															{
																textHAlignLeft( );
																width( "25%" );
															}
														} );
														control( new ButtonBuilder( "westButton", "" )
														{
															{
																visibleToMouse( true );
																interactOnClick( "setKeyBinding(westButton)" );
															}
														} );
													}
												} );

												panel( new PanelBuilder( "inputChat" )
												{
													{

														childLayoutHorizontal( );
														control( new LabelBuilder( "chatLabel", "Chat: " )
														{
															{
																textHAlignLeft( );
																width( "25%" );
															}
														} );
														control( new ButtonBuilder( "chatButton", "" )
														{
															{
																visibleToMouse( true );
																interactOnClick( "setKeyBinding(chatButton)" );
															}
														} );
													}
												} );

												panel( new PanelBuilder( "inputScore" )
												{
													{

														childLayoutHorizontal( );
														control( new LabelBuilder( "scoreLabel", "Scoreboard: " )
														{
															{
																textHAlignLeft( );
																width( "25%" );
															}
														} );
														control( new ButtonBuilder( "scoreButton", "" )
														{
															{
																visibleToMouse( true );
																interactOnClick( "setKeyBinding(scoreButton)" );
															}
														} );
													}
												} );
											}
										} );
									}
								} );
							}
						} );
					}
				} );
			}
		}.build( nifty ) );
	}

	/**
	 * Builds and adds in-game hud screen and creates a popup for the score
	 * board, console, and menu.
	 * 
	 * @param nifty
	 */
	public static void createHudView( Nifty nifty )
	{
		nifty.addScreen( "hud", new ScreenBuilder( "hud" )
		{

			{
				controller( new HudViewController( ) );

				layer( new LayerBuilder( "layer" )
				{

					{
						backgroundColor( "#0000" );
						childLayoutCenter( );

						panel( new PanelBuilder( "miniBorder" )
						{

							{
								width( "27%" );
								height( "27%" );
								alignRight( );
								valignBottom( );
								childLayoutCenter( );

								image( new ImageBuilder( )
								{

									{
										filename( "Materials/test.png" );
										width( "100%" );
										height( "100%" );
										alignCenter( );
										valignCenter( );
									}
								} );
							}
						} );

						panel( new PanelBuilder( "overview" )
						{

							{
								backgroundColor( "#404040ff" );
								width( "46%" );
								height( "12%" );
								alignCenter( );
								valignBottom( );
								childLayoutCenter( );

								text( new TextBuilder( )
								{

									{
										text( "Overview" );
										font( "aurulent-sans-16.fnt" );
										width( "100%" );
										height( "100%" );
										color( "#000f" );
									}
								} );
							}
						} );

						panel( new PanelBuilder( "unitInfo" )
						{

							{
								backgroundColor( "#303030ff" );
								width( "27%" );
								height( "12%" );
								alignLeft( );
								valignBottom( );
								childLayoutHorizontal( );
							}
						} );
						panel( new PanelBuilder( "topRightButtons" )
						{
							{
								width( "5%" );
								alignRight( );
								valignTop( );
								childLayoutHorizontal( );
								control( new ButtonBuilder( "ingameOptions", "Op" )
								{

									{
										width( "50%" );
										alignRight( );
										valignTop( );
										visibleToMouse( true );
										interactOnClick( "openOptions()" );
									}
								} );
								control( new ButtonBuilder( "ingameMenu", "Menu" )
								{
									{
										width( "50%" );
										alignRight( );
										valignTop( );
										visibleToMouse( true );
										interactOnClick( "showMenu()" );
									}
								} );
							}
						} );
					}
				} );
			}
		}.build( nifty ) );

		new PopupBuilder( "ingameChat" )
		{
			{
				childLayoutCenter( );
				panel( new PanelBuilder( "ingameChatPanel" )
				{

					{
						width( "30%" );
						height( "30%" );
						alignLeft( );
						valignCenter( );
						childLayoutCenter( );

						control( new ChatBuilder( "gameChat", 8 )
						{
							{
								sendLabel( "Send Message" );
							}
						} );
					}
				} );
			}
		}.registerPopup( nifty );

		new PopupBuilder( "scoreboardPopup" )
		{
			{
				childLayoutCenter( );
				panel( new PanelBuilder( "scorePanel" )
				{
					{
						backgroundColor( "#303030ff" );
						childLayoutHorizontal( );
						width( "60%" );
						height( "60%" );
						alignCenter( );
						valignCenter( );

						panel( new PanelBuilder( "player1Col" )
						{
							{
								backgroundColor( "#404040ff" );
								childLayoutVertical( );
								width( "25%" );
								height( "100%" );
								alignLeft( );
								valignCenter( );

								panel( new PanelBuilder( "name1" )
								{
									{
										width( "100%" );
										height( "10%" );
										childLayoutVertical( );
										valignTop( );

										control( new LabelBuilder( "name1Label" )
										{
											{
												alignCenter( );
												valignBottom( );
												// height( "32px" );
												width( "100%" );
												wrap( true );
												color( "#f2f2f2ff" );
											}
										} );
									}
								} );

								panel( new PanelBuilder( "units1" )
								{
									{
										width( "100%" );
										height( "10%" );
										childLayoutVertical( );
										valignTop( );

										control( new LabelBuilder( "unit1Label" )
										{
											{
												alignCenter( );
												valignBottom( );
												// height( "32px" );
												width( "100%" );
												wrap( true );
												color( "#f2f2f2ff" );
											}
										} );
									}
								} );

								panel( new PanelBuilder( "building1" )
								{
									{
										width( "100%" );
										height( "10%" );
										childLayoutVertical( );
										valignTop( );

										control( new LabelBuilder( "building1Label" )
										{
											{
												alignCenter( );
												valignBottom( );
												// height( "32px" );
												width( "100%" );
												wrap( true );
												color( "#f2f2f2ff" );
											}
										} );
									}
								} );
							}
						} );
						panel( new PanelBuilder( "player2Col" )
						{
							{
								backgroundColor( "#303030ff" );
								childLayoutVertical( );
								width( "25%" );
								height( "100%" );
								alignLeft( );
								valignCenter( );

								panel( new PanelBuilder( "name2" )
								{
									{
										width( "100%" );
										height( "10%" );
										childLayoutVertical( );
										valignTop( );

										control( new LabelBuilder( "name2Label" )
										{
											{
												alignCenter( );
												valignBottom( );
												// height( "32px" );
												width( "100%" );
												wrap( true );
												color( "#f2f2f2ff" );
											}
										} );
									}
								} );

								panel( new PanelBuilder( "units2" )
								{
									{
										width( "100%" );
										height( "10%" );
										childLayoutVertical( );
										valignTop( );

										control( new LabelBuilder( "unit2Label" )
										{
											{
												alignCenter( );
												valignBottom( );
												// height( "32px" );
												width( "100%" );
												wrap( true );
												color( "#f2f2f2ff" );
											}
										} );
									}
								} );

								panel( new PanelBuilder( "building2" )
								{
									{
										width( "100%" );
										height( "10%" );
										childLayoutVertical( );
										valignTop( );

										control( new LabelBuilder( "building2Label" )
										{
											{
												alignCenter( );
												valignBottom( );
												// height( "32px" );
												width( "100%" );
												wrap( true );
												color( "#f2f2f2ff" );
											}
										} );
									}
								} );
							}
						} );
						panel( new PanelBuilder( "player3Col" )
						{
							{
								backgroundColor( "#404040ff" );
								childLayoutVertical( );
								width( "25%" );
								height( "100%" );
								alignLeft( );
								valignCenter( );

								panel( new PanelBuilder( "name3" )
								{
									{
										width( "100%" );
										height( "10%" );
										childLayoutVertical( );
										valignTop( );

										control( new LabelBuilder( "name3Label" )
										{
											{
												alignCenter( );
												valignBottom( );
												// height( "32px" );
												width( "100%" );
												wrap( true );
												color( "#f2f2f2ff" );
											}
										} );
									}
								} );

								panel( new PanelBuilder( "units3" )
								{
									{
										width( "100%" );
										height( "10%" );
										childLayoutVertical( );
										valignTop( );

										control( new LabelBuilder( "unit3Label" )
										{
											{
												alignCenter( );
												valignBottom( );
												// height( "32px" );
												width( "100%" );
												wrap( true );
												color( "#f2f2f2ff" );
											}
										} );
									}
								} );

								panel( new PanelBuilder( "building3" )
								{
									{
										width( "100%" );
										height( "10%" );
										childLayoutVertical( );
										valignTop( );

										control( new LabelBuilder( "building3Label" )
										{
											{
												alignCenter( );
												valignBottom( );
												// height( "32px" );
												width( "100%" );
												wrap( true );
												color( "#f2f2f2ff" );
											}
										} );
									}
								} );
							}
						} );
						panel( new PanelBuilder( "player4Col" )
						{
							{
								backgroundColor( "#303030ff" );
								childLayoutVertical( );
								width( "25%" );
								height( "100%" );
								alignLeft( );
								valignCenter( );

								panel( new PanelBuilder( "name4" )
								{
									{
										width( "100%" );
										height( "10%" );
										childLayoutVertical( );
										valignTop( );

										control( new LabelBuilder( "name4Label" )
										{
											{
												alignCenter( );
												valignBottom( );
												// height( "32px" );
												width( "100%" );
												wrap( true );
												color( "#f2f2f2ff" );
											}
										} );
									}
								} );

								panel( new PanelBuilder( "units4" )
								{
									{
										width( "100%" );
										height( "10%" );
										childLayoutVertical( );
										valignTop( );

										control( new LabelBuilder( "unit4Label" )
										{
											{
												alignCenter( );
												valignBottom( );
												// height( "32px" );
												width( "100%" );
												wrap( true );
												color( "#f2f2f2ff" );
											}
										} );
									}
								} );
								panel( new PanelBuilder( "building4" )
								{
									{
										width( "100%" );
										height( "10%" );
										childLayoutVertical( );
										valignTop( );

										control( new LabelBuilder( "building4Label" )
										{
											{
												alignCenter( );
												valignBottom( );
												// height( "32px" );
												width( "100%" );
												wrap( true );
												color( "#f2f2f2ff" );
											}
										} );
									}
								} );
							}
						} );
					}
				} );
			}
		}.registerPopup( nifty );

		new PopupBuilder( "menu" )
		{
			{
				childLayoutCenter( );
				panel( new PanelBuilder( "menuPanel" )
				{
					{
						backgroundColor( "#3e3e3eff" );
						childLayoutVertical( );
						width( "100%" );
						height( "100%" );
						alignCenter( );
						valignCenter( );

						control( new ButtonBuilder( "exitToNetwork", "Exit to network" )
						{
							{
								visibleToMouse( true );
								interactOnClick( "exitToNetwork( )" );
							}
						} );
						control( new ButtonBuilder( "exitGame", "Exit game" )
						{
							{
								visibleToMouse( true );
								interactOnClick( "exitGame()" );
							}
						} );

					}
				} );
			}
		}.registerPopup( nifty );

	}

	public static void createConsole( Nifty nifty )
	{
		nifty.addScreen( "consoleScreen", new ScreenBuilder( "consoleScreen" )
		{
			{
				controller( new ConsoleViewController( ) );
				layer( new LayerBuilder( "consoleLayer" )
				{
					{
						childLayoutCenter( );
						backgroundColor( "#00000000" );

						panel( new PanelBuilder( "consolePanel" )
						{
							{
								childLayoutCenter( );
								width( "50%" );
								height( "50%" );

								control( new ConsoleBuilder( "console" )
								{

									{
										width( "100%" );
										lines( 25 );
										alignCenter( );
										valignCenter( );
									}
								} );
							}
						} );
					}
				} );
			}
		}.build( nifty ) );
	}

	/**
	 * Creates and adds network lobby.
	 * 
	 * @param nifty
	 */
	public static void createNetworkView( Nifty nifty )
	{
		nifty.addScreen( "network", new ScreenBuilder( "network" )
		{

			{
				controller( new NetworkViewController( ) );

				layer( new LayerBuilder( "layer" )
				{

					{
						childLayoutVertical( );
						backgroundColor( "#838796ff" );

						panel( new PanelBuilder( "networkHeader" )
						{
							{
								width( "100%" );
								height( "32px" );
								valignBottom( );
								childLayoutHorizontal( );
								backgroundColor( "#616374ff" );
								paddingLeft( "32px" );
								paddingRight( "32px" );

								text( new TextBuilder( "networkTitle" )
								{
									{
										width( "50%" );
										height( "100%" );
										text( "Network Lobby" );
										textHAlignLeft( );
										textVAlignCenter( );
										font( "Interface/Fonts/Default.fnt" );

									}
								} );

								panel( new PanelBuilder( "networkButtons" )
								{
									{
										width( "50%" );
										childLayoutHorizontal( );

										panel( new PanelBuilder( "spacer" )
										{
											{
												width( "*" );
											}
										} );

										control( new ButtonBuilder( "Logout", "Logout" )
										{

											{
												alignCenter( );
												valignCenter( );
												visibleToMouse( true );
												interactOnClick( "logout()" );
											}
										} );

									}
								} );

							}
						} );

						panel( new PanelBuilder( "mainPanel" )
						{

							{
								width( "66%" );
								height( "100%" );
								padding( "32px" );
								alignCenter( );
								valignCenter( );
								childLayoutVertical( );

								panel( new PanelBuilder( "networkList" )
								{
									{
										childLayoutVertical( );
										width( "100%" );
										style( "nifty-panel-brown" );

										panel( new PanelBuilder( "networkControls" )
										{
											{
												childLayoutHorizontal( );
												width( "100%" );
												style( "nifty-panel-inset-beige" );

												control( new ButtonBuilder( "join", "Join" )
												{

													{
														alignCenter( );
														valignCenter( );
														visibleToMouse( true );
														interactOnClick( "joinServer( )" );
													}
												} );

												control( new ButtonBuilder( "refresh", "Refresh List" )
												{
													{
														alignCenter( );
														visibleToMouse( true );
														interactOnClick( "refreshServerList()" );
													}
												} );

											}
										} );

										control( new ListBoxBuilder( "networkScroll" )
										{

											{
												displayItems( 10 );
												showVerticalScrollbar( );
												hideHorizontalScrollbar( );
											}
										} );

										panel( new PanelBuilder( "networkOptions" )
										{
											{
												childLayoutHorizontal( );
												style( "nifty-panel-inset-beige" );

												panel( new PanelBuilder( "netOptions" )
												{

													{
														alignCenter( );
														valignTop( );
														childLayoutVertical( );

														control( new RadioGroupBuilder( "RadioGroup-1" ) );
														panel( new PanelBuilder( )
														{
															{
																childLayoutHorizontal( );
																alignRight( );
																control( new LabelBuilder( "filterButtons", "Filters" ) );

																panel( new PanelBuilder( )
																{
																	{
																		childLayoutVertical( );
																		backgroundColor( "#8001" );
																		paddingLeft( "7px" );
																		paddingRight( "7px" );
																		paddingTop( "4px" );
																		paddingBottom( "4px" );
																		width( "105px" );
																		alignRight( );
																		valignTop( );
																		onActiveEffect( new EffectBuilder( "border" )
																		{
																			{
																				effectParameter( "color", "#0008" );
																			}
																		} );
																		panel( new PanelBuilder( )
																		{
																			{
																				childLayoutHorizontal( );
																				alignRight( );
																				control( new LabelBuilder( "all", "All" ) );
																				control( new RadioButtonBuilder( "option-1" )
																				{
																					{
																						group( "RadioGroup-1" );
																					}
																				} );
																			}
																		} );
																		panel( new PanelBuilder( )
																		{
																			{
																				childLayoutHorizontal( );
																				alignRight( );
																				control( new LabelBuilder( "isFull", "Full" ) );
																				control( new RadioButtonBuilder( "option-2" )
																				{
																					{
																						group( "RadioGroup-1" );
																					}
																				} );
																			}
																		} );
																		panel( new PanelBuilder( )
																		{
																			{
																				childLayoutHorizontal( );
																				alignRight( );
																				control( new LabelBuilder( "isEmpty", "Empty" ) );
																				control( new RadioButtonBuilder( "option-3" )
																				{
																					{
																						group( "RadioGroup-1" );
																					}
																				} );
																			}
																		} );
																	}
																} );
															}
														} );
														panel( new PanelBuilder( )
														{
															{
																childLayoutHorizontal( );
																alignRight( );
																control( new LabelBuilder( "hasPlayerLabel", "Has Players" ) );
																control( new CheckboxBuilder( "hasPlayer" ) );
															}
														} );

													}
												} );

												panel( new PanelBuilder( "netOptions" )
												{

													{
														childLayoutVertical( );
														panel( new PanelBuilder( )
														{
															{
																childLayoutHorizontal( );
																control( new LabelBuilder( "labelSearch", "Search" ) );
																control( new TextFieldBuilder( "search", "" )
																{
																	{
																		alignRight( );
																		maxLength( 32 );
																		width( "75%" );
																	}
																} );
															}
														} );

														control( new ButtonBuilder( "filters", "Apply" )
														{
															{
																alignCenter( );
																visibleToMouse( true );
																interactOnClick( "applyFilters( )" );
															}
														} );

													}
												} );

											}
										} );
									}
								} );

								control( new ChatBuilder( "networkChat", 8 )
								{
									{
										sendLabel( "Send Message" );
									}
								} );

							}
						} );
					}
				} );
			}
		}.build( nifty ) );
	}

	/**
	 * Creates and adds in-game options menu. Does not have graphics control.
	 * 
	 * @param nifty
	 */
	public static void createOptionsView( Nifty nifty )
	{
		nifty.addScreen( "options", new ScreenBuilder( "options" )
		{

			{
				controller( new OptionsViewController( ) );

				layer( new LayerBuilder( "layer" )
				{
					{
						backgroundColor( "#1a1a1aff" );
						childLayoutHorizontal( );

						panel( new PanelBuilder( "optionsButtons" )
						{
							{
								width( "25%" );
								height( "100%" );
								alignCenter( );
								valignCenter( );
								childLayoutVertical( );
								backgroundColor( "#303030ff" );
								padding( "16px" );

								text( new TextBuilder( )
								{
									{
										text( "Options" );
										font( "Interface/Fonts/Default.fnt" );
										textHAlignCenter( );
										width( "100%" );
									}
								} );

								control( new ButtonBuilder( "selectGraphics", "Apply" )
								{
									{
										alignCenter( );
										valignCenter( );
										visibleToMouse( true );
										interactOnClick( "" );
									}
								} );
								control( new ButtonBuilder( "backtostart", "Back" )
								{
									{
										alignCenter( );
										valignCenter( );
										visibleToMouse( true );
										interactOnClick( "nextScreen(hud)" );
									}
								} );

							}
						} );
						panel( new PanelBuilder( "optionsTabs" )
						{
							{
								width( "75%" );
								height( "100%" );
								padding( "16px" );
								alignCenter( );
								valignCenter( );
								childLayoutVertical( );
								backgroundColor( "#202020ff" );

								panel( new PanelBuilder( "audioLabel" )
								{
									{
										backgroundColor( "#303030ff" );
										childLayoutHorizontal( );
										control( new LabelBuilder( "audioOptions", "Audio" )
										{
											{
												textHAlignCenter( );
												width( "100%" );
											}
										} );
									}
								} );
								panel( new PanelBuilder( "optionsMainAudio" )
								{
									{
										backgroundColor( "#404040ff" );
										childLayoutHorizontal( );
										control( new LabelBuilder( "labelMainVolume", "Main Volume: " )
										{
											{
												textHAlignRight( );
												width( "75%" );
											}
										} );

										control( new SliderBuilder( "mainVolumeSlider", false )
										{
											{
												buttonStepSize( 5.0f );
												max( 100.0f );
											}
										} );
									}
								} );
								panel( new PanelBuilder( "optionsMusic" )
								{
									{
										backgroundColor( "#303030ff" );
										childLayoutHorizontal( );
										control( new LabelBuilder( "labelMusic", "Music Volume: " )
										{
											{
												textHAlignRight( );
												width( "75%" );
											}
										} );

										control( new SliderBuilder( "musicSlider", false )
										{
											{
												buttonStepSize( 5.0f );
												max( 100.0f );
											}
										} );
									}
								} );
								panel( new PanelBuilder( "optionsSounds" )
								{
									{
										backgroundColor( "#404040ff" );
										childLayoutHorizontal( );
										control( new LabelBuilder( "labelSounds", "Sounds Volume: " )
										{
											{
												textHAlignRight( );
												width( "75%" );
											}
										} );

										control( new SliderBuilder( "soundsSlider", false )
										{
											{
												buttonStepSize( 5.0f );
												max( 100.0f );
											}
										} );
									}
								} );
								panel( new PanelBuilder( "inputLabel" )
								{
									{
										backgroundColor( "#303030ff" );
										childLayoutHorizontal( );
										control( new LabelBuilder( "inputOptions", "Input" )
										{
											{
												textHAlignCenter( );
												width( "100%" );
											}
										} );
									}
								} );
							}
						} );
					}
				} );
			}
		}.build( nifty ) );
	}

	/**
	 * Creates and adds tile view. Will contain different things for different
	 * tiles selected.
	 * 
	 * @param nifty
	 */
	public static void createTileView( Nifty nifty )
	{

		nifty.addScreen( "tile", new ScreenBuilder( "tile" )
		{

			{
				controller( new TileViewController( ) );

				layer( new LayerBuilder( "layer" )
				{

					{
						backgroundColor( "#0000" );
						childLayoutCenter( );

						panel( new PanelBuilder( "miniBorder" )
						{

							{
								width( "27%" );
								height( "27%" );
								alignRight( );
								valignBottom( );
								childLayoutCenter( );

								image( new ImageBuilder( )
								{

									{
										filename( "Materials/test.png" );
										width( "100%" );
										height( "100%" );
										alignCenter( );
										valignCenter( );
									}
								} );
							}
						} );

						panel( new PanelBuilder( "topRightButtons" )
						{
							{
								width( "5%" );
								alignRight( );
								valignTop( );
								childLayoutHorizontal( );
								control( new ButtonBuilder( "ingameOptions", "Op" )
								{

									{
										width( "50%" );
										alignRight( );
										valignTop( );
										visibleToMouse( true );
										interactOnClick( "nextScreen(options)" );
									}
								} );
								control( new ButtonBuilder( "exit", "Exit" )
								{

									{
										width( "50%" );
										alignLeft( );
										valignTop( );
										visibleToMouse( true );
										interactOnClick( "exitView()" );
									}
								} );
							}
						} );

						panel( new PanelBuilder( "overview" )
						{

							{
								backgroundColor( "#303030ff" );
								width( "47%" );
								height( "27%" );
								alignCenter( );
								valignBottom( );
								childLayoutCenter( );
								style( "nifty-panel-red-no-shadow" );

								text( new TextBuilder( )
								{

									{
										text( "Overview" );
										font( "aurulent-sans-16.fnt" );
										width( "100%" );
										height( "100%" );
										color( "#000f" );
									}
								} );
							}
						} );

						panel( new PanelBuilder( "currentProd" )
						{

							{
								backgroundColor( "#404040ff" );
								width( "27%" );
								height( "27%" );
								alignLeft( );
								valignBottom( );
								childLayoutCenter( );
								style( "nifty-panel-red-no-shadow" );

								text( new TextBuilder( )
								{

									{
										text( "Current Production" );
										font( "aurulent-sans-16.fnt" );
										width( "100%" );
										height( "100%" );
										color( "#000f" );
									}
								} );
							}
						} );

						panel( new PanelBuilder( "buildable" )
						{

							{
								width( "27%" );
								height( "46%" );
								alignLeft( );
								valignCenter( );
								childLayoutCenter( );

								control( new ListBoxBuilder( "buildBox" )
								{

									{
										displayItems( 14 );
										optionalHorizontalScrollbar( );
										optionalVerticalScrollbar( );
										width( "100%" );
										height( "100%" );
										valignBottom( );
									}
								} );
							}
						} );

						panel( new PanelBuilder( "existing" )
						{

							{
								width( "27%" );
								height( "46%" );
								alignRight( );
								valignCenter( );
								childLayoutCenter( );

								control( new ListBoxBuilder( "existBox" )
								{

									{
										displayItems( 14 );
										optionalHorizontalScrollbar( );
										optionalVerticalScrollbar( );
										width( "100%" );
										height( "100%" );
										valignBottom( );
									}
								} );
							}
						} );
					}
				} );
			}
		}.build( nifty ) );

		// TODO popup of move buttons? Shows unit in middle, buttons in hexgon
		// pattern with move two on them.
		// TODO if not currently moving or being built.
		new PopupBuilder( "move" )
		{
			{
				childLayoutCenter( );
				panel( new PanelBuilder( "mainPanel" )
				{
					{
						// backgroundColor( "#3e3e3eff" );
						childLayoutVertical( );
						width( "100%" );
						height( "100%" );
						alignCenter( );
						valignCenter( );

						panel( new PanelBuilder( "layerPanel" )
						{
							{
								childLayoutHorizontal( );
								width( "50%" );
								height( "100%" );
								alignCenter( );
								valignCenter( );

								panel( new PanelBuilder( "panel1" )
								{
									{
										childLayoutVertical( );
										width( "33%" );
										height( "100%" );
										alignCenter( );
										valignCenter( );

										panel( new PanelBuilder( "top1" )
										{
											{
												childLayoutCenter( );
												width( "100%" );
												height( "33%" );
												alignCenter( );
												valignTop( );

												control( new ButtonBuilder( "moveNorthWest", "NW +2" )
												{
													{
														valignBottom( );
														visibleToMouse( true );
														interactOnClick( "moveCommand(NW)" );
													}
												} );
											}
										} );

										panel( new PanelBuilder( "mid1" )
										{
											{
												childLayoutCenter( );
												width( "100%" );
												height( "33%" );
												alignCenter( );
												valignCenter( );
											}
										} );

										panel( new PanelBuilder( "bot1" )
										{
											{
												childLayoutCenter( );
												width( "100%" );
												height( "33%" );
												alignCenter( );
												valignBottom( );

												control( new ButtonBuilder( "moveSouthWest", "SW +2" )
												{
													{
														valignTop( );
														visibleToMouse( true );
														interactOnClick( "moveCommand(SW)" );
													}
												} );
											}
										} );
									}
								} );

								panel( new PanelBuilder( "panel2" )
								{
									{
										childLayoutVertical( );
										width( "33%" );
										height( "100%" );
										alignCenter( );
										valignCenter( );

										panel( new PanelBuilder( "top2" )
										{
											{
												childLayoutCenter( );
												width( "100%" );
												height( "33%" );
												alignCenter( );
												valignTop( );

												control( new ButtonBuilder( "moveNorth", "N +2" )
												{
													{
														visibleToMouse( true );
														interactOnClick( "moveCommand(N)" );
													}
												} );
											}
										} );

										panel( new PanelBuilder( "mid2" )
										{
											{
												childLayoutCenter( );
												width( "100%" );
												height( "33%" );
												alignCenter( );
												valignCenter( );

												panel( new PanelBuilder( "unitPanel" )
												{
													{
														backgroundColor( "#303030ff" );
														childLayoutCenter( );
														width( "100%" );
														height( "75%" );
														alignCenter( );
														valignCenter( );

														control( new LabelBuilder( "unit" )
														{
															{
																textHAlignCenter( );
																width( "100%" );
																height( "100%" );
																font( "Interface/Fonts/AngerthasMoria.fnt" );
															}
														} );

														control( new ButtonBuilder( "close", "Close" )
														{
															{
																valignBottom( );
																visibleToMouse( true );
																interactOnClick( "closeMove()" );
															}
														} );
													}
												} );

											}
										} );

										panel( new PanelBuilder( "bot2" )
										{
											{
												childLayoutCenter( );
												width( "100%" );
												height( "33%" );
												alignCenter( );
												valignBottom( );

												control( new ButtonBuilder( "moveSouth", "S +2" )
												{
													{
														visibleToMouse( true );
														interactOnClick( "moveCommand(S)" );
													}
												} );
											}
										} );
									}
								} );

								panel( new PanelBuilder( "panel3" )
								{
									{
										childLayoutVertical( );
										width( "33%" );
										height( "100%" );
										alignCenter( );
										valignCenter( );

										panel( new PanelBuilder( "top3" )
										{
											{
												childLayoutCenter( );
												width( "100%" );
												height( "33%" );
												alignCenter( );
												valignTop( );

												control( new ButtonBuilder( "moveNorthEast", "NE +2" )
												{
													{
														valignBottom( );
														visibleToMouse( true );
														interactOnClick( "moveCommand(NE)" );
													}
												} );
											}
										} );

										panel( new PanelBuilder( "mid3" )
										{
											{
												childLayoutCenter( );
												width( "100%" );
												height( "33%" );
												alignCenter( );
												valignCenter( );
											}
										} );

										panel( new PanelBuilder( "bot3" )
										{
											{
												childLayoutCenter( );
												width( "100%" );
												height( "33%" );
												alignCenter( );
												valignBottom( );

												control( new ButtonBuilder( "moveSouthEast", "SE +2" )
												{
													{
														valignTop( );
														visibleToMouse( true );
														interactOnClick( "moveCommand(SE)" );
													}
												} );
											}
										} );
									}
								} );
							}
						} );
					}
				} );
			}
		}.registerPopup( nifty );
	}

}
