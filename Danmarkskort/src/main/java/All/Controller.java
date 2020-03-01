package All;

import Draw.*;
import OSM.Model;
import OSM.Node;
import OSM.Way;
import Path.NoPathException;
import Path.RouteGuidance;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Paint;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *  Handles all user input and fxml interaction
 */

public class Controller {
	private int selectionModelIndex;
	private LoadColorMap loadCM;
	private dialogBox dialogBox;
	private HashMap<String, ArrayList<String>> streetsNoHouse;
	private ArrayList<String> searchResult;
	private Autocompleter ac;
	@FXML
	private Button searchButton;
	@FXML
	private Label meterMeasureText;
	@FXML
	private Label roadText;
	@FXML
	private Slider zoomSlider;
	@FXML
	private ListView<String> searchResultsView;
	@FXML
	private ListView<String> routeResultsView;
	@FXML
	private TextField searchField;
	public ObservableList<String> searchList = FXCollections.observableArrayList();
	public ObservableList<String> routeObservable=FXCollections.observableArrayList();
	private Model model;
	double x, y;
	@FXML
	private MapCanvas mapCanvas;
	private ChangeFile changefile;
	private Model osmmodel;
	private Stage stage;

	@FXML
	private TextField routeSearchFrom;
	@FXML
	private TextField routeSearchTo;
	@FXML
	private Button switchButton;
	@FXML
	private Button pointButton;
	@FXML
	private Button searchButtonRoute;
	@FXML
	private ListView routelist;
	@FXML
	private Button bikeButton;
	@FXML
	private Button walkButton;
	@FXML
	private Button carButton;

	private boolean shiftBoolean;

	private boolean routeSearch;
	private boolean up;
	private boolean down;
	private boolean enter;
	private boolean firstSelect;
	private boolean car;
	private boolean bike;
	private boolean walk;
	private OSM.Node fromNode;
	private OSM.Node toNode;
	private String transportType;
	private Way way;

	public Controller(){
	}

	/**
	 * Initialises all the fields needed for the controllers functionality and methods
	 * @param model Current loaded model
	 * @param stage Current loaded stage
	 */
	public void init(Model model, Stage stage){
		this.model = model;
		this.stage = stage;
		ac = new Autocompleter();
		mapCanvas.init(model);
        loadCM = new LoadColorMap(mapCanvas);
		searchResultsView.setVisible(false);
		routeResultsView.setVisible(false);
		mapCanvas.zoom(0.3, mapCanvas.getScreenCenterX(), mapCanvas.getScreenCenterY());
		dialogBox = new dialogBox();
		streetsNoHouse = new HashMap<>(model.getStringNoHouseAddressMap());
		changefile = new ChangeFile();
		meterMeasureText.setTextFill(Paint.valueOf("Black"));
		meterMeasureText.setText(mapCanvas.meterMeasure());

		selectionModelIndex = 0;
		up = false;
		down = false;
		routeSearch = false;
		firstSelect = true;
		car = true;
		bike = false;
		walk = false;

		shiftBoolean = false;
		stage.getScene().widthProperty().addListener((obs, oldVal, newVal) -> {
			setStageHeight();
			mapCanvas.repaint();
		});
		stage.getScene().heightProperty().addListener((obs, oldVal, newVal) -> {
			setStageHeight();
			mapCanvas.repaint();
		});

		routeSearchSwitchVisible();
		setStageHeight();
		mapCanvas.repaint();
	}


	/**
	 * Switch between elements by making them visible and non visible.
	 * This method controls whether elements should be visible or not.
	 */
	private void routeSearchSwitchVisible(){
		searchResultsView.setVisible(false);
		routeResultsView.setVisible(false);
		routeResultsView.setDisable(!routeSearch);

		searchField.setVisible(!routeSearch);
		searchField.setDisable(routeSearch);
		searchButton.setVisible(!routeSearch);
		searchButton.setDisable(routeSearch);

		routeSearchFrom.setVisible(routeSearch);
		routeSearchTo.setVisible(routeSearch);
		switchButton.setVisible(routeSearch);
		pointButton.setVisible(routeSearch);
		searchButtonRoute.setVisible(routeSearch);
		routelist.setVisible(false);
		carButton.setVisible(false);
		walkButton.setVisible(false);
		bikeButton.setVisible(false);

		if (routeSearch){
			String searchText = searchField.getText();
			routeSearchTo.setText(searchText);
			try {
				toNode = model.getStringAddressMap().get(searchField.getText()).getNode();
			} catch (NullPointerException e) {
			}
		} else {
			String searchtext = routeSearchTo.getText();
			if (searchtext.equals("")){
				searchtext = routeSearchFrom.getText();
			}
			searchField.setText(searchtext);
		}
		}


	/**
	 * Reacts to a keypress from the general window. If V is pressed then it should show the Kd tree
	 * and if enter is pressed it should search for the address specified in the searchfield
	 * @param e The event from the pressed key
	 */
	@FXML
	private void onKeyPressed(KeyEvent e) {
		switch (e.getCode()) {
			case V:
				mapCanvas.setShowKd(!mapCanvas.isShowKd());
				break;
		}
		shiftPressed(e);
	}

	/**
	 * Reacts to a keypress from the general window. If SHIFT is pressed it will find the nearest street
	 * and show the street name in the top left corner.
	 * @param e The event from the pressed key.
	 */
	private void shiftPressed(KeyEvent e){
		if(e.getCode() == KeyCode.SHIFT){
			shiftBoolean = true;
			e.consume();
		}
		else {
			shiftBoolean = false;
			e.consume();
		}
	}

	/**
	 * When the searchbutton is pressed, search for an address
	 * @param e The mouse event
	 */
	@FXML
	private void searchButtonClicked(MouseEvent e) {
		searchAddress(searchField, searchResultsView);
	}

	/**
	 * Whenever a user writes something in the searchfield this will show a list of suggestions in a menu under the textfield
	 * If no suggestions were found it will say that none were found
	 * @param e The event from the pressed key
	 */
	@FXML
	private void searchFieldKeyTyped(KeyEvent e) {
		searchSuggestion(searchField.getText(), searchResultsView);
	}

	/**
	 * Makes search suggestions for road names in the "From" textfield.
	 * @param e
	 */
	@FXML
	private void routeSearchFromKeyTyped(KeyEvent e){
		searchSuggestion(routeSearchFrom.getText(), searchResultsView);
	}

	/**
	 * Makes search suggestions for road names in the "To" textfield.
	 * @param e
	 */
	@FXML
	private void routeSearchToKeyTyped(KeyEvent e){
		searchSuggestion(routeSearchTo.getText(), routeResultsView);
	}

	/**
	 * Makes search suggestions for road names in a listview.
	 * @param searchString
	 * @param resultsView
	 */
	private void searchSuggestion(String searchString, ListView resultsView){
		selectionModelIndex = 0;
		firstSelect = true;
		if (searchString.equals("")) {
			resultsView.setVisible(false);
			searchList.clear();
		} else {
			searchResult = ac.match(streetsNoHouse, searchString);
			resultsView.setVisible(true);
			if (searchResult.isEmpty()) {
				searchList.setAll("Ingen resultater");
			} else {
				searchList.setAll(searchResult);
				resultsView.setItems(searchList);
			}
		}
	}

	/**
	 * Handles key events from searchfield allowing the user to use up/down to interact with the associated listview
	 * and enter to search for an address matching the text in the textfield
	 * @param e The keyevent
	 */
	@FXML
	private void searchFieldKeyReleased(KeyEvent e){
		textFieldKeyEventOverrider(searchField, searchResultsView, e);
		down = false;
		up = false;
		enter = false;
	}

	/**
	 * Handles key events from searchfield allowing the user to use up/down to interact with the associated listview
	 * and enter to search for an address matching the text in the textfield
	 * @param e The keyevent
	 */
	@FXML
	private void routeSearchFromKeyReleased(KeyEvent e){
		textFieldKeyEventOverrider(routeSearchFrom, searchResultsView, e);
		down = false;
		up = false;
		enter = false;
	}

	/**
	 * Handles key events from searchfield allowing the user to use up/down to interact with the associated listview
	 * and enter to search for an address matching the text in the textfield
	 * @param e The keyevent
	 */
	@FXML
	private void routeSearchToKeyReleased(KeyEvent e){
		textFieldKeyEventOverrider(routeSearchTo, routeResultsView, e);
		down = false;
		up = false;
		enter = false;
	}

	/**
	 * Overrides keyevents for textfields used for searching
	 * @param textfield The textfield that is recieving keyevents
	 * @param listView The listView associated with that textfield
	 */
	private void textFieldKeyEventOverrider(TextField textfield, ListView listView, KeyEvent e){
		textfield.addEventFilter(KeyEvent.ANY, new EventHandler<KeyEvent>() {
			@Override public void handle(KeyEvent keyEvent) {
				switch (keyEvent.getCode()) {
					case ENTER:
						enter = true;
						break;
					case UP:
						up = true;
						break;
					case DOWN:
						down = true;
						break;
				}
			}
		});
		shiftPressed(e);
		if (up || down) {
			upAndDown(textfield, listView);
		}
		if (enter){
		    if (textfield == routeSearchFrom){
                fromNode = searchAddress(textfield, listView);
            } else if (textfield == routeSearchTo){
                toNode = searchAddress(textfield, listView);
            }
			searchAddress(textfield, listView);
		}
	}

	/**
	 * Goes up and down in the listview and changes textfield based on what gets selected by up/down pressed
	 * @param textField Textfield to change text in
	 * @param listView Listview to navigate through
	 */
	private void upAndDown(TextField textField, ListView listView){
		if (up){
			if(selectionModelIndex>0) {
				selectionModelIndex--;
			}
		}else if (down){
			if (firstSelect){
				selectionModelIndex = 0;
				firstSelect = false;
			} else if (selectionModelIndex< listView.getItems().size()) {
				selectionModelIndex++;
			}
		}

		if (up || down) {
			try {
				listView.getSelectionModel().clearAndSelect(selectionModelIndex);
				if (!listView.getSelectionModel().getSelectedItem().equals("")) {
					textField.setText((String) listView.getSelectionModel().getSelectedItem());
					listView.scrollTo(selectionModelIndex - 2);
					listView.setVisible(true);
				}
			} catch (NullPointerException excp){
			}
			up = false;
			down = false;

		}
	}

	/**
	 * Shows the listview associated with the searchField when pressed
	 * @param e MousEvent
	 */
	@FXML
	private void searchFieldClicked(MouseEvent e){
		searchResultsView.setVisible(true);
	}

	/**
	 * Shows the listview associated with the routeSearchTo when pressedand disables the listview associated with routeSearchFrom
	 * @param e MousEvent
	 */
	@FXML
	private void routeSearchToClicked(MouseEvent e){
		routeResultsView.setVisible(true);
		searchResultsView.setVisible(false);
	}

	/**
	 * Shows the listview associated with the routeSearchFrom when pressed and disables the listview associated with routeSearchTo
	 * @param e MousEvent
	 */
	@FXML
	private void routeSearchFromClicked(MouseEvent e){
		searchResultsView.setVisible(true);
		routeResultsView.setVisible(false);
	}

	/**
	 * When the mouse button is pressed within the listview and the searchList isn't empty,
	 * it will set the text in the searchfield to the pressed item.
	 * @param e The event from the mouse
	 */
	@FXML
	private void searchResultsViewPressed(MouseEvent e) {
		if (!searchList.subList(0,1).toString().equals("[Ingen resultater]")) {
			if (routeSearch){
				routeSearchFrom.setText(searchResultsView.getSelectionModel().getSelectedItem());
				fromNode = searchAddress(routeSearchFrom, searchResultsView);
			}else {
				searchField.setText(searchResultsView.getSelectionModel().getSelectedItem());
				searchAddress(searchField, searchResultsView);
			}
			selectionModelIndex = searchResultsView.getSelectionModel().getSelectedIndex();
		}
	}

	/**
	 * When enter is pressed within the the listview it searches for the address in the associated textField,
	 * if up and down is pressed is goes through the items in the listview
	 * @param e The keyevent
	 */
	@FXML
	private void searchResultsViewKeyPressed(KeyEvent e){
		switch (e.getCode()){
			case ENTER:
				if (routeSearch){
					fromNode = searchAddress(routeSearchFrom, searchResultsView);
				} else {
					searchAddress(searchField, searchResultsView);
				}
				break;
			case UP:
				up = true;
				break;
			case DOWN:
				down = true;
				break;
		}
		if (routeSearch){
			upAndDown(routeSearchFrom, searchResultsView);
		} else {
			upAndDown(searchField, searchResultsView);
		}
	}

	/**
	 * Sets the routeSearchTo textField to the item pressed in the routeResultsListView
	 * @param e MouseEventE
	 */
	@FXML
	private void routeResultsViewPressed(MouseEvent e){
		if (!searchList.subList(0,1).toString().equals("[Ingen resultater]")) {
			routeSearchTo.setText(routeResultsView.getSelectionModel().getSelectedItem());
			selectionModelIndex = routeResultsView.getSelectionModel().getSelectedIndex();
			toNode = searchAddress(routeSearchTo, routeResultsView);
		}
	}

	/**
	 * When enter is pressed within the the listview it searches for the address in the associated textField,
	 * if up and down is pressed is goes through the items in the listview
	 * @param e The keyevent
	 */
	@FXML
	private void routeResultsViewKeyPressed(KeyEvent e){
		switch (e.getCode()){
			case ENTER:
				toNode = searchAddress(routeSearchTo, routeResultsView);
				break;
			case UP:
				up = true;
				break;
			case DOWN:
				down = true;
				break;
		}
		if (up || down) {
			upAndDown(routeSearchTo, routeResultsView);
		}
	}


	private void setStageHeight(){
		mapCanvas.setStageHeight(stage.getScene().getHeight());
	}
	/**
	 * Gets the nodes associated with the addresses from routesearchFrom and routeSearchTo textfields and
	 * generates a route from routeSearchFrom to routeSearchTo
	 * @param e MouseEevent e
	 */
	@FXML
	private void searchButtonRouteClicked(MouseEvent e){
		try {
			OSM.Node fromNodetmp = model.getStringAddressMap().get(routeSearchFrom.getText()).getNode();
			if (fromNodetmp != null){
				fromNode = fromNodetmp;
			}
		}  catch (NullPointerException excp){
			dialogBox.errorBox("Error", "Invalid address: " + routeSearchFrom.getText());
			return;
		}
		try {
			OSM.Node toNodetmp = model.getStringAddressMap().get(routeSearchTo.getText()).getNode();
			if (toNodetmp != null) {
				toNode = toNodetmp;
			}
			zoomTilFar();
		}  catch (NullPointerException excp){
			dialogBox.errorBox("Error", "Invalid address: " + routeSearchTo.getText());
			return;
		}

		model.removeDrawable(WayType.POI_MARKER);
		generateRoute(fromNode, toNode);
		makeRouteInstructions();
		updateTransportButtonColors();
		routelist.setVisible(true);
		walkButton.setVisible(true);
		bikeButton.setVisible(true);
		carButton.setVisible(true);
	}

	/**
	 * Generates the route from one OSM.Node to another OSM.Node.
	 * The method computes the road from the correct transport type.
	 * @param from
	 * @param to
	 */
	private void generateRoute(OSM.Node from, OSM.Node to)
	{
		removePins();
		model.setDrawRoute(false);
		 transportType = "";
		if(car)
		{
			transportType = "vehicle";
		}
		if(bike)
		{
			transportType = "bicycle";
		}
		if(walk)
		{
			transportType = "walking";
		}

		Drawable fromDrawable =  model.nearest(from.getLon(), from.getLat(),transportType).getRepresentative();
		Long fromID = fromDrawable.getNearestIdOfNode(from.getLon(), from.getLat());
		Drawable toDrawable =  model.nearest(to.getLon(), to.getLat(),transportType).getRepresentative();
		Long toID = toDrawable.getNearestIdOfNode(to.getLon(), to.getLat());
		Node nodeFrom = null;
		Node nodeTo = null;
		for (Way w : model.getRoadWayList()) {
			for (Node n : w) {
				if (n.getAsLong() == fromID) {
					nodeFrom = n;
				}
				if (n.getAsLong() == toID) {
					nodeTo = n;
				}
			}
		}
		try
		{
			model.getEdgeWeightedDigraph().pathFromTo(nodeFrom, nodeTo, transportType);
			model.setDrawRoute(true);
			Node first = model.getPathNodes().get(0);
			Node last = model.getPathNodes().get(model.getPathNodes().size()-1);

			POINode pinFrom = new POINode(first.getLon(), first.getLat());
			POINode pinTo = new POINode(last.getLon(), last.getLat());
			model.setPinFrom(pinFrom);
			model.setPinTo(pinTo);
			mapCanvas.repaint();
		}
		catch (NoPathException e)
		{
			dialogBox.errorBox("No path exists", "No path could be found from " + routeSearchFrom.getText() + " to " + routeSearchTo.getText() + " with the given transport type: " + transportType);
		}

	}

	/**
	 * Removes the pins placed in the map.
	 */
	public void removePins()
	{
		if(!model.getWays().get(WayType.POI_MARKER).isEmpty())
		{
			model.removeDrawable(WayType.POI_PIN);
		}
	}

	/**
	 * Whenever the user scrolls it will zoom in or out on the map based on which way there was scrolled
	 * @param e The event from the scrollwheel
	 */
	@FXML
	private void onScroll(ScrollEvent e){
		double factor = Math.pow(1.01, e.getDeltaY());
		if (factor > 1) {
			zoomSlider.setValue((mapCanvas.plusZoom(zoomSlider.getValue(), zoomSlider.getMax(), e.getX(), e.getY())));
		} else {
			zoomSlider.setValue((mapCanvas.minusZoom(zoomSlider.getValue(), zoomSlider.getMin(), e.getX(), e.getY())));
		}
		meterMeasureText.setText(mapCanvas.meterMeasure());
		setStageHeight();
		mapCanvas.repaint();
	}

	/**
	 * When the mouse is held and moved, the map will be moved accordingly
	 * @param e The event from the mouse
	 */
	@FXML
	private void onMouseDragged(MouseEvent e) {
		if (e.isPrimaryButtonDown())
        {
            mapCanvas.pan(e.getX() - x, e.getY() - y);
        }
		x = e.getX();
		y = e.getY();
		setStageHeight();
		mapCanvas.repaint();
	}

	/**
	 * When the second mouse button is pressed, a point of interest is created
	 * @param e The event from the mouse
	 */
	@FXML
	private void onMousePressed(MouseEvent e)
    {
        if(!e.isPrimaryButtonDown())
		{
			mapCanvas.createPOI(e.getX(), e.getY());
		}

		x = e.getX();
		y = e.getY();
		searchResultsView.setVisible(false);
		routeResultsView.setVisible(false);
	}


	/**
	 * When the plus zoom button is pressed, zoom in
	 * @param e The event from the mouse
	 */
	@FXML
	private void onZoomPlusPressed(MouseEvent e) {
		zoomSlider.setValue((mapCanvas.plusZoom(zoomSlider.getValue(), zoomSlider.getMax(), mapCanvas.getScreenCenterX(), mapCanvas.getScreenCenterY())));
		meterMeasureText.setText(mapCanvas.meterMeasure());
		setStageHeight();
		mapCanvas.repaint();
	}

	/**
	 * When the minus zoom button is pressed, zoom out
	 * @param e The event from the mouse
	 */
	@FXML
	private void onZoomMinusPressed(MouseEvent e) {
		zoomSlider.setValue((mapCanvas.minusZoom(zoomSlider.getValue(), zoomSlider.getMin(), mapCanvas.getScreenCenterX(), mapCanvas.getScreenCenterY())));
		meterMeasureText.setText(mapCanvas.meterMeasure());
		setStageHeight();
		mapCanvas.repaint();
	}

	/**
	 * When the zoomslider is no longer dragged, the zoom in/out will adjust
	 * @param e The event from the mouse
	 */
	@FXML
	private void onZoomSliderReleased(MouseEvent e) {
		mapCanvas.scaleZoom(zoomSlider.getValue(),mapCanvas.getScreenCenterX(), mapCanvas.getScreenCenterY());
		meterMeasureText.setText(mapCanvas.meterMeasure());
		setStageHeight();
		mapCanvas.repaint();
	}

	/**
	 * When the Open option is pressed, change the currently loaded file
	 * @param e The action event
	 */


	@FXML
	public void onOpenClicked(ActionEvent e) {
		String confirmationText = "Are you sure you wanna close the current map and open a new one?";
		if (dialogBox.confirmationBox(confirmationText)) {
			try {
				init(changefile.pickFile(stage), stage);
			} catch (Exception excp){
				dialogBox.errorBox("Error", "File couldn't be loaded");
			}
		}
	}

	/**
	 * Creates a marker in the middle of the screen when an address is searched.
	 * The searched address will always be in the middle of the screen as well.
	 */
	private void createPoIonScreenCenter(){
		double x = mapCanvas.getScreenCenterX();
		double y = mapCanvas.getScreenCenterY();
		mapCanvas.setPoiType("marker");
		mapCanvas.createPOI(x,y);
		setStageHeight();
		mapCanvas.repaint();
	}

	/**
	 * Changes the color mode of the mapcanvas to normal
	 * @param e The actionevent
	 */
    @FXML
    private void onNormalClicked(ActionEvent e)
    {
        loadCM.loadFillMap();
		setStageHeight();
        mapCanvas.repaint();
    }

	/**
	 * Switches the view to a view where you can search directions.
	 * @param e handles the event when the button is clicked.
	 */
	@FXML
	private void routeButtonClicked(MouseEvent e){
		routeSearch = !routeSearch;
		routeSearchSwitchVisible();
    }

	/**
	 * Switches the view to a view where you can only search an address.
	 * @param e handles the event when the button is clicked.
	 */
	@FXML
	private void pointButtonClicked(MouseEvent e){
		routeSearch = !routeSearch;
		routeSearchSwitchVisible();
	}

	/**
	 * Switches the text in the "From" textfield with the text in the "To" textfield.
	 * @param e handles the event when mouse clicked.
	 */
	@FXML
	private void routeSwitchButtonClicked(MouseEvent e){
    	OSM.Node nodeFrom = fromNode;
    	String fromText = routeSearchFrom.getText();
    	routeSearchFrom.setText(routeSearchTo.getText());
    	routeSearchTo.setText(fromText);
    	fromNode = toNode;
    	toNode = nodeFrom;
	}

	/**
	 * Changes the color of the transport buttons to correctly represent their state
	 */
	private void updateTransportButtonColors(){
		String active = "-fx-background-color: deepskyblue";
		String inActive = "-fx-background-color: #2B2B2B";
		if (car) {
			carButton.setStyle(active);
		} else {
			carButton.setStyle(inActive);
		}
		if (bike) {
			bikeButton.setStyle(active);
		} else {
			bikeButton.setStyle(inActive);
		}
		if (walk) {
			walkButton.setStyle(active);
		} else {
			walkButton.setStyle(inActive);
		}
	}

	/**
	 * Changes the type of transport to car. The button will then turn blue.
	 * @param e handles the event when mouse is clicked.
	 */
	@FXML
	private void onCarButtonClicked(MouseEvent e){
		car = !car;
		bike = false;
		walk = false;
		updateTransportButtonColors();
	}

	/**
	 * Changes the type of transport to bike. The button will then turn blue.
	 * @param e handles the event when mouse is clicked.
	 */
	@FXML
	private void onBikeButtonClicked(MouseEvent e){
		bike = !bike;
		car = false;
		walk = false;
		updateTransportButtonColors();
	}

	/**
	 * Changes the type of transport to walk. The button will then turn blue.
	 * @param e handles the event when mouse is clicked.
	 */
	@FXML
	private void onWalkButtonClicked(MouseEvent e){
		walk = !walk;
		bike = false;
		car = false;
		updateTransportButtonColors();
	}

	/**
	 * Changes the color mode of the mapcanvas to colorblind
	 * @param e The actionevent
	 */
	@FXML
	private void onCbClicked(ActionEvent e)
	{
		loadCM.loadCBFillMap();
	}

	/**
	 * Changes the color mode of the mapcanvas to black and white
	 * @param e The actionevent
	 */
    @FXML
    private void onBwClicked(ActionEvent e)
    {
        loadCM.loadBwFillMap();
    }

	/**
	 * Sets marker to point of interest marker
	 * @param e
	 */
    @FXML
	private void onMarkerClicked(ActionEvent e){mapCanvas.setPoiType("marker");}

	/**
	 * Sets market to visit market
	 * @param e
	 */
	@FXML
	private void onVisitClicked(ActionEvent e){mapCanvas.setPoiType("visit");}

	/**
	 *Finds the correct name of the street where clicked. It will be displayed in black with the intro text "Street name:".
	 * @param e then event when mouse is clicked.
	 * @throws NonInvertibleTransformException
	 */
	@FXML
	private void onMouseHoverRoad(MouseEvent e) throws NonInvertibleTransformException {
		if(shiftBoolean) {
			Point2D p = mapCanvas.getTransform().inverseTransform(e.getX(), e.getY());
			long id = model.nearest((float) p.getX(), (float) p.getY(), "vehicle").getRepresentative().getNearestIdOfNode(0,0);
			Node node = null;
			for (Way way : model.getRoadWayList()) {
				for (Node n : way) {
					if (n.getAsLong() == id) {
						node = n;
						break;
					}
				}
			}
			String street = node.getStreet();
			roadText.setTextFill(Paint.valueOf("Black"));
			roadText.setText("Street name: " + street);
		}
	}


	/**
	 * Searches for an address and returns a node if an address is found, if no Address is found but searchText is
	 * a street with no house number it will set the associated listview to hold all the addresses of that street with
	 * housenumbers, in case no address or street could be found it opens an error window
	 */
	private OSM.Node searchAddress(TextField textField, ListView listView) {
		try {
			if (textField.getText().trim().equals("")){
				return null;
			}
			OSM.Node node = model.getStringAddressMap().get(textField.getText()).getNode();
			mapCanvas.panCoords(node.getLon(), node.getLat());
			zoomTilClose();
			createPoIonScreenCenter();
			listView.setVisible(false);
			return node;
			} catch (NullPointerException e) {
			try {
				searchResult = ac.streetMatch(streetsNoHouse, textField.getText());
				searchList.setAll(searchResult);
				listView.setItems(searchList);
				selectionModelIndex = 0;
				firstSelect = true;
				listView.setVisible(true);
			}
			catch (NullPointerException npe){
				try{
					listView.getSelectionModel().selectFirst();
					OSM.Node node = model.getStringAddressMap().get(listView.getSelectionModel().getSelectedItem()).getNode();
					mapCanvas.panCoords(node.getLon(), node.getLat());
					zoomTilClose();
					createPoIonScreenCenter();
					listView.setVisible(false);
					textField.setText((String) listView.getSelectionModel().getSelectedItem());
					return node;
				}
				catch (NullPointerException npe2) {
					try {
						listView.getSelectionModel().selectFirst();
						if (!listView.getSelectionModel().getSelectedItem().equals("Ingen resultater")){
							textField.setText((String) listView.getSelectionModel().getSelectedItem());
							searchResult = ac.streetMatch(streetsNoHouse, (String) listView.getSelectionModel().getSelectedItem());
							searchList.setAll(searchResult);
							listView.setItems(searchList);
							selectionModelIndex = 0;
							firstSelect = true;
							listView.setVisible(true);
						}
					} catch (NullPointerException npe3) {
						dialogBox.errorBox("", "That address doesn't exist");
						listView.setVisible(false);

					}
				}
			}
		}
		return null;
	}

	/**
	 * Zooms in until the distance from Draw.MapCanvas.getDistance is less than 2
	 */
	private void zoomTilClose(){
		while (mapCanvas.getDistance()>2){
			zoomSlider.setValue((mapCanvas.plusZoom(zoomSlider.getValue(), zoomSlider.getMax(), mapCanvas.getScreenCenterX(), mapCanvas.getScreenCenterY())));
			meterMeasureText.setText(mapCanvas.meterMeasure());
		}
	}

	/**
	 * Zooms out until the distance from Draw.MapCanvas.getDistance is more than 80
	 */
	private void zoomTilFar(){
		while (mapCanvas.getDistance()<80){
			zoomSlider.setValue((mapCanvas.minusZoom(zoomSlider.getValue(), zoomSlider.getMin(), mapCanvas.getScreenCenterX(), mapCanvas.getScreenCenterY())));
			meterMeasureText.setText(mapCanvas.meterMeasure());
		}
	}

	/**
	 *
	 */
	public void makeRouteInstructions(){
        RouteGuidance rg = new RouteGuidance(model, transportType);

        if(!routeObservable.isEmpty()){
            routeObservable.clear();
        }
	    routeObservable.addAll(rg.getRouteGuidanceOutput());
        routelist.setVisible(true);
	    routelist.setItems(routeObservable);
    }


}

