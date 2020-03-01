import React from 'react';
import ReactDOM from 'react-dom';
import styles from '../App/App.module.css';
import SearchBar from '../SearchBar/SearchBar';
import SearchResults from '../SearchResults/SearchResults';
import MultiList from '../MultiList/MultiList';
import Notify from '../Notify/Notify';
import CategoryDropdown from '../CategoryDropdown/CategoryDropdown';
import SliderCollection from '../Slider/SliderCollection';

class Recommendation extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      searchResults: [],
      savedItems: [],
      itemType: 'track',
      genres: [],
      url: ''
    };
    this.addItem = this.addItem.bind(this);
    this.removeItem = this.removeItem.bind(this);
    this.getItems = this.getItems.bind(this);
    this.searchTrack = this.searchTrack.bind(this);
    this.searchArtist = this.searchArtist.bind(this);
    this.changeItemType = this.changeItemType.bind(this);
    this.setSavedItems = this.setSavedItems.bind(this);
    this.getGenres = this.getGenres.bind(this);
    this.setUrl = this.setUrl.bind(this);
  }

  addItem(item){
    let savedItems = this.state.savedItems;
    if (savedItems.find(savedItem => savedItem.id === item.id)) {
      return;
    }
    if(savedItems.length == 5){
      this.props.setNotify();
      this.props.setNotifyMessage('Only a maximum of 5 choices are allowed');
      return;
    }
    savedItems.push(item);
    this.setState({savedItems: savedItems});
  }

  removeItem(item){
    let savedItems = this.state.savedItems;
    savedItems = savedItems.filter(currentItem => currentItem.id !== item.id);
    this.setState({savedItems: savedItems});
  }

  getItems(){
    this.state.savedItems.map(item => {
      console.log('item: ' + item.name || item.genreName + ' id: ' + item.id)
    })
  }

  searchTrack(term, criteria){
    this.props.searchTrack(term, criteria).then(searchResults => {
      this.setState({
        searchResults: searchResults
      })
    });
  }

  searchArtist(term){
    this.props.searchArtist(term).then(searchResults => {
      this.setState({
        searchResults: searchResults
      })
    });
  }

  changeItemType(type){
    this.setState({itemType: type}, console.log('item type: ' + this.state.itemType));
  }

  setSavedItems(){
    if(this.state.savedItems.length < 1){
      this.props.setNotify();
      this.props.setNotifyMessage('Error!', 'Please fill in at least one item');
      return;
    }
    this.getItems();
    this.props.setSavedItems(this.state.savedItems, this.state.url);
  }

  getGenres(){
    this.props.getGenres().then(genres => {
      this.setState({
        genres: genres
      })
    });
  }

  setUrl(url){
    this.setState({url: url})
  }

  componentDidMount(){
    this.getGenres();
    console.log('mounted');
};

  render(){
    return (
      <div>
        <div className={styles.app}>
          <SliderCollection setUrl={this.setUrl}
                            setNotify={this.props.setNotify}
                            setNotifyMessage={this.props.setNotifyMessage}/>
            <SearchBar changeItemType={this.changeItemType}
                       type='recommendation'
                       searchArtist={this.searchArtist}
                       searchTrack={this.searchTrack}
                       popupMenu={this.popupMenu}
                       setNotify={this.props.setNotify}
                       setNotifyMessage={this.props.setNotifyMessage}/>
            <CategoryDropdown
                genres={this.state.genres}
                onAdd={this.addItem}
            />
          <div className={styles.appPlaylist}>
            <SearchResults 
                isRecommendation = {true}
                itemType = {this.state.itemType}
                searchResults={this.state.searchResults}
                onAdd={this.addItem}
                addAllTracks={this.addAllTracks}/>
            <MultiList
                setSavedItems={this.setSavedItems}
                items={this.state.savedItems}
                onRemove={this.removeItem}
                onNameChange={this.updatePlaylistName}
                onSave={this.savePlaylist}/>
          </div>
        </div>
      </div>
    );
  }
}

export default Recommendation;

/* class App {

    login() {
        return this.state.login ? <MyApp /> : <Login />;
    }
}
*/