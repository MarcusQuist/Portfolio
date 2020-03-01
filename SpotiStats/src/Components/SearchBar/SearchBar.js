import React from 'react';
import styles from './SearchBar.module.css';

class SearchBar extends React.Component {
    constructor(props){
      super(props);

      this.state = {
        term: '',
        criteria: 'track',
        placeholder: 'Enter a song title',
        category: 'track'
      };
      this.search = this.search.bind(this);
      this.handleTermChange = this.handleTermChange.bind(this);
      this.albumCriteria = this.albumCriteria.bind(this);
      this.artistCriteria = this.artistCriteria.bind(this);
      this.trackCriteria = this.trackCriteria.bind(this);
      this.artistCategory = this.artistCategory.bind(this);
      this.trackCategory = this.trackCategory.bind(this);
    }

    search(){
      if(this.state.term == '')
      {
        this.props.setNotify();
        this.props.setNotifyMessage('Please fill in a search word');
        return;
      }
      if(this.state.category == 'artist'){
        this.props.searchArtist(this.state.term, this.state.criteria);
      }
      if(this.state.category == 'track'){
        this.props.searchTrack(this.state.term, this.state.criteria);
      }
    }

    handleTermChange(event){
      this.setState({term: event.target.value});
    }

    albumCriteria(){
      this.setState({criteria: 'album', placeholder: 'Enter an album title'});
    }

    artistCriteria(){
      this.setState({criteria: 'artist', placeholder: 'Enter an artist\'s name'});
    }

    trackCriteria(){
      this.setState({criteria: 'track', placeholder: 'Enter a song title'});
    }

    printToConsole()
    {
      console.log('state: ' + this.state.criteria);
    }

    artistCategory(){
      this.setState({category: 'artist', placeholder: 'Enter an artist\'s name'});
    }

    trackCategory(){
      this.setState({category: 'track', placeholder: 'Enter a song title'});
    }

    itemTypeArtist(){
      this.setState({category: 'artist', placeholder: 'Enter an artist\'s name'});
      this.props.changeItemType('artist');
    }

    itemTypeTrack(){
      this.setState({category: 'track', placeholder: 'Enter a song title'});
      this.props.changeItemType('track');
    }

    render() {
      if(this.props.type == 'app')
      {
        return (
          <div className={styles.SearchBar}>
              <h2>Choose a search criteria</h2>
              <div className={styles.ButtonContainer}>
                <button type="button" className={styles.ContainerButton + ' ' + styles.SearchContainerButton}
                onClick={this.albumCriteria}>Album</button>
                <button type="button" className={styles.ContainerButton + ' ' + styles.SearchContainerButton}
                onClick={this.artistCriteria}>Artist</button>
                <button type="button" className={styles.ContainerButton + ' ' + styles.SearchContainerButton}
                onClick={this.trackCriteria}>Track</button>
              </div>
              <input  onChange={this.handleTermChange}
                      placeholder={this.state.placeholder}/>
              <button type="button" className={styles.SearchButton + ' ' + styles.SearchButtonMargin}
                      onClick={this.search}>SEARCH</button>
              <div className={styles.ButtonContainer + ' ' + styles.ButtonContainerMargin}>
                <button type="button" className={styles.ContainerButton + ' ' + styles.ContainerButtonBottom}
                onClick={this.props.popupMenu}>Generate Personal Playlist</button>
                <button type="button" className={styles.ContainerButton + ' ' + styles.ContainerButtonBottom}
                onClick={this.props.recommendations}>Generate Recommendations</button>
              </div>
          </div>
          );
      }
      if(this.props.type == 'recommendation')
      {
        return (
          <div className={styles.SearchBar}>
              <h2>Select a search category</h2>
              <div className={styles.ButtonContainer}>
                <button type="button" className={styles.ContainerButton + ' ' + styles.SearchContainerButton}
                onClick={this.artistCategory}>Artist</button>
                <button type="button" className={styles.ContainerButton + ' ' + styles.SearchContainerButton}
                onClick={this.trackCategory}>Track</button>
              </div>
              <input  onChange={this.handleTermChange}
                      placeholder={this.state.placeholder}/>
              <button type="button" className={styles.SearchButton + ' ' + styles.SearchButtonMargin}
                      onClick={this.search}>SEARCH</button>
          </div>
          );
      }
    }
  }

export default SearchBar;
