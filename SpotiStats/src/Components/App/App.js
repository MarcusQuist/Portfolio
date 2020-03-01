import React from 'react';
import ReactDOM from 'react-dom';
import styles from './App.module.css';
import SearchBar from '../SearchBar/SearchBar';
import SearchResults from '../SearchResults/SearchResults';
import Playlist from '../Playlist/Playlist';
import PopupMenu from '../PopupMenu/PopupMenu';
import Recommendation from '../Recommendation/Recommendation';
import Notify from '../Notify/Notify';

class App extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      searchResults: [],
      playlistName: '',
      playlistTracks: [],
      popupMenu: false,
      recommendations: false,
      notify: false,
      notifyMessage: ''
    };
    this.addTrack = this.addTrack.bind(this);
    this.addAllTracks = this.addAllTracks.bind(this);
    this.removeTrack = this.removeTrack.bind(this);
    this.updatePlaylistName = this.updatePlaylistName.bind(this);
    this.savePlaylist = this.savePlaylist.bind(this);
    this.searchTrack = this.searchTrack.bind(this);
    this.login = this.login.bind(this);
    this.updateAccessToken = this.updateAccessToken.bind(this);
    this.popupMenu = this.popupMenu.bind(this);
    this.renderPopupMenu = this.renderPopupMenu.bind(this);
    this.recommendations = this.recommendations.bind(this);
    this.generatePersonalizedPlaylist = this.generatePersonalizedPlaylist.bind(this);
    this.setSavedItems = this.setSavedItems.bind(this);
    this.setNotify = this.setNotify.bind(this);
    this.notify = this.notify.bind(this);
    this.setNotifyMessage = this.setNotifyMessage.bind(this);
  }

  addTrack(track){
    let tracks = this.state.playlistTracks;
    if (tracks.find(savedTrack => savedTrack.id === track.id)) {
      return;
    }
    tracks.push(track);
    this.setState({playlistTracks: tracks});
  }

  addAllTracks(){
    let searchResults = this.state.searchResults;
    this.setState({playlistTracks: searchResults})
  }

  removeTrack(track){
    let tracks = this.state.playlistTracks;
    tracks = tracks.filter(currentTrack => currentTrack.id !== track.id);
    this.setState({playlistTracks: tracks});
  }

  updatePlaylistName(name){
    this.setState({playlistName: name});
  }

  savePlaylist(e){
    // Gets the track uri of each track. 
    const trackUris = this.state.playlistTracks.map(track => track.uri);
    if(this.state.playlistName == ''){
      this.setNotify();
      this.setNotifyMessage("Error", "Please specify a playlist name");
    }
    else{
      this.props.savePlaylist(this.state.playlistName, trackUris)
      .then(() => {
        this.setState({
          playlistName: 'New Playlist',
          playlistTracks: []
        })
      }) 
    }
  }

  setNotify(){
    this.setState({notify: !this.state.notify})
  }

  setNotifyMessage(title, message){
    this.setState({notifyTitle: title, notifyMessage: message});
  }

  // Sets the state's searchResults to the returned searchResults of the method in Spotify
  searchTrack(term, criteria){
    this.props.searchTrack(term, criteria).then(searchResults => {
      this.setState({
        searchResults: searchResults
      })
    });
  }

  generatePersonalizedPlaylist(timeRange){
    this.props.generatePersonalizedPlaylist(timeRange).then(searchResults => {
      this.setState({
        searchResults: searchResults
      })
    }); 
  }


  updateAccessToken(value){
    this.setState({
      accessToken: value
    })
  }

  recommendations(){
    this.setState({recommendations: !this.state.popupMenu})
  }

  setSavedItems(items, url){
    console.log(items);
    let trackIds = [];
    let genreIds = [];
    let artistIds = [];

     items.map(item => {
      if(item.hasOwnProperty('album'))
      {
        trackIds.push(item.id);
      }
      if(item.hasOwnProperty('genreName'))
      {
        genreIds.push(item.id);
      }
      if(!item.hasOwnProperty('album') && !item.hasOwnProperty('genreName'))
      {
        artistIds.push(item.id);
      }
    }); 

    this.props.generateRecommendations(artistIds, trackIds, genreIds, url).then(searchResults => {
      this.setState({
        playlistTracks: searchResults
      })
    }); 

    this.setState({
      playlistTracks: items,
      recommendations: false
    })
  }

  login(){
    const { token, getToken } = this.props;
    if(token && !this.state.recommendations){
      console.log('normal');
      return (
        <div className={styles.app}>
            <SearchBar type = 'app'
                       searchTrack={this.searchTrack}
                       popupMenu={this.popupMenu}
                       recommendations={this.recommendations}
                       setNotify={this.setNotify}
                       setNotifyMessage={this.setNotifyMessage}/>
          <div className={styles.appPlaylist}>
            <SearchResults 
                itemType = 'track'
                searchResults={this.state.searchResults}
                onAdd={this.addTrack}
                addAllTracks={this.addAllTracks}/>
            <Playlist 
                playlistName={this.state.playlistName} 
                playlistTracks={this.state.playlistTracks}
                onRemove={this.removeTrack}
                onNameChange={this.updatePlaylistName}
                onSave={this.savePlaylist}/>
          </div>
        </div>
      );}
      if(token && this.state.recommendations)
      {
        console.log('recommendation');
        return(
        <Recommendation 
            setNotify={this.setNotify}
            setNotifyMessage={this.setNotifyMessage}
            setSavedItems={this.setSavedItems}
            logout={this.logout}
            searchTrack={this.props.searchTrack}
            searchArtist={this.props.searchArtist}
            getGenres={this.props.getGenres}/>
            );
      }
      else{
        return (
          <div className={styles.appLogin}>
            <div>
            <h1>Login to start browsing yours <span className={styles.highlight}>stats</span>!</h1>
            <button className={styles.buttonLogin}
                    onClick={getToken}
            >Login</button>
            </div>
          </div>
        );
      }
  }

  popupMenu(){
    this.setState({popupMenu: !this.state.popupMenu})
  }

  renderPopupMenu(){
    if(this.state.popupMenu == true){
      return <PopupMenu 
              popupMenu={this.popupMenu}
              generatePlaylist={this.generatePersonalizedPlaylist}/>;
    }
  }

  setNotify(){
    this.setState({notify: !this.state.notify})
  }

  setNotifyMessage(title, message){
    this.setState({notifyTitle: title, notifyMessage: message});
  }

  notify(){
    if(this.state.notify == true){
      return<Notify
             title={this.state.notifyTitle}
             message={this.state.notifyMessage}
             close={this.setNotify}
             message={this.state.notifyMessage}/>
    }
  }

  render(){
    return (
      <div id={styles.root}>
        {this.notify()}
        {this.renderPopupMenu()}
        <div className={styles.topbar}>
        <h1 className={styles.bk}>Spoti<span className={styles.highlight}>stats</span></h1>
        <button className={styles.buttonLogout}
                onClick={this.props.logout}>Logout</button>
        </div>  
        {this.login()}
      </div>
    );
  }
}

export default App;
