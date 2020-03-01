import React from 'react';
import ReactDOM from 'react-dom';
import styles from './SearchResultList.module.css';
import Track from '../Track/Track';
import Artist from '../Artist/Artist';

class SearchResultList extends React.Component {
    render() {
      return (
        <div className={styles.List}>
          {
            this.props.items.map(item => {
              if(item.hasOwnProperty('album'))
              {
                return <Track track={item}
                key={item.id} 
                onAdd={this.props.onAdd}
                onRemove={this.props.onRemove}
                isRemoval={this.props.isRemoval}
                />
              }
              else
              {
                return <Artist artist={item}
                key={item.id} 
                name={item.name}
                url={item.url}
                onAdd={this.props.onAdd}
                onRemove={this.props.onRemove}
                isRemoval={this.props.isRemoval}
                />
              }
            })
          }
        </div>
        );
    }
}

export default SearchResultList;
