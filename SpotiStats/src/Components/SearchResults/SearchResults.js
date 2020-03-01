import React from 'react';
import styles from './SearchResults.module.css';
import SearchResultList from '../SearchResultList/SearchResultList';

class SearchResults extends React.Component {
    render() {
      if(!this.props.isRecommendation)
      {
        return (
          <div className={styles.SearchResults}>
              <div className={styles.container}>
                <h2>Results</h2>
                <button type="button" 
                        className={styles.searchButton}
                        onClick={this.props.addAllTracks}>Add All</button>
              </div>
              <SearchResultList 
                  itemType={this.props.itemType}
                  items={this.props.searchResults}
                  onAdd={this.props.onAdd}
                  isRemoval={false}/>
          </div>
          );
      }
      else
      {
        return (
          <div className={styles.SearchResults}>
              <div className={styles.container}>
                <h2>Results</h2>
              </div>
              <SearchResultList 
                  itemType={this.props.itemType}
                  items={this.props.searchResults}
                  onAdd={this.props.onAdd}
                  isRemoval={false}/>
          </div>
          );
      }
    }
  }

  export default SearchResults;