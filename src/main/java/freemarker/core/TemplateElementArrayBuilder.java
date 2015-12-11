package freemarker.core;

import freemarker.template.utility.CollectionUtils;

/**
 * Holds an buffer (array) of {@link TemplateElement}-s with the count of the utilized items in it. The un-utilized tail
 * of the array must only contain {@code null}-s.
 * 
 * @since 2.3.24
 */
class TemplateElements {
    
    static final TemplateElements EMPTY = new TemplateElements(null, 0);

    private final TemplateElement[] buffer;
    private final int count;

    /**
     * @param buffer
     *            The buffer; {@code null} exactly if {@code count} is 0.
     * @param count
     *            The number of utilized buffer elements; if 0, then {@code null} must be {@code null}.
     */
    TemplateElements(TemplateElement[] buffer, int count) {
        if (count == 0 && buffer != null) { // !!T temporal assertion
            throw new IllegalArgumentException(); 
        }
        this.buffer = buffer;
        this.count = count;
    }

    TemplateElement[] getBuffer() {
        return buffer;
    }

    int getCount() {
        return count;
    }
    
    TemplateElement getLast() {
        return buffer != null ? buffer[count - 1] : null;
    }
    
    /**
     * Used for some backward compatibility hacks.
     */
    TemplateElement asSingleElement() {
        if (count == 0) {
            return new TextBlock(CollectionUtils.EMPTY_CHAR_ARRAY, false); 
        } else {
            TemplateElement first = buffer[0];
            if (count == 1) {
                return first;
            } else {
                MixedContent mixedContent = new MixedContent();
                mixedContent.setChildren(this);
                mixedContent.setLocation(first.getTemplate(), first, getLast());
                return mixedContent;
            }
        }
    }
    
    /**
     * Used for some backward compatibility hacks.
     */
    MixedContent asMixedContent() {
        MixedContent mixedContent = new MixedContent();
        if (count != 0) {
            TemplateElement first = buffer[0];
            mixedContent.setChildren(this);
            mixedContent.setLocation(first.getTemplate(), first, getLast());
        }
        return mixedContent;
    }

}
