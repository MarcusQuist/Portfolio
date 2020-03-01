import React from 'react';
import ReactDOM from 'react-dom';
import styles from './MultiList.module.css';
import SelectedList from '../SelectedList/SelectedList';

class MultiList extends React.Component {
  constructor(props){
    super(props);
    this.handleNameChange = this.handleNameChange.bind(this);
  }

    handleNameChange(event){
        this.props.onNameChange(event.target.value);
    }
    render() {
      return (
        <div className={styles.MultiList}>
            <h2 className={styles.multiListTitle}>Selected items (5 max)</h2> 
            <button type="button" className={styles.MultiListSave}
            onClick={this.props.setSavedItems}
            >Get Recommendations</button>
            <SelectedList items={this.props.items}
                        onRemove={this.props.onRemove}
                        isRemoval={true}/>
        </div>
        );
    }
  }

  export default MultiList;
