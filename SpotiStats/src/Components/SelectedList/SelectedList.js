import React from 'react';
import ReactDOM from 'react-dom';
import styles from './SelectedList.module.css';
import Track from '../Track/Track';
import Artist from '../Artist/Artist';
import Genre from '../Genre/Genre';

class SelectedList extends React.Component {
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
                if(!item.hasOwnProperty('name')){
                  return <Genre genre={item}
                      key={item.id} 
                      genreName={item.genreName}
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

export default SelectedList;
