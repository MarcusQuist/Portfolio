import React from 'react';
import ReactDOM from 'react-dom';
import styles from './Playlist.module.css';
import SearchResultList from '../SearchResultList/SearchResultList';

class Playlist extends React.Component {
  constructor(props){
    super(props);
    this.handleNameChange = this.handleNameChange.bind(this);
  }

    handleNameChange(event){
        this.props.onNameChange(event.target.value);
    }
    render() {
      return (
        <div className={styles.Playlist}>
            <input  value={this.props.playlistName}
                    onChange={this.handleNameChange}
                    placeholder="Enter a name for your playlist"/>
            <button type="button" className={styles.PlaylistSave}
            onClick={this.props.onSave}
            >SAVE TO SPOTIFY</button>
            <SearchResultList 
                        itemType='track'
                        items={this.props.playlistTracks}
                        onRemove={this.props.onRemove}
                        isRemoval={true}/>
        </div>
        );
    }
  }

  export default Playlist;
