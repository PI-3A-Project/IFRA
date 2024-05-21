package br.com.ifra.enums;

public enum AdvancedFilter {
    download(new String[]{"epub", ""}), filter(new String[]{"epub", ""}), langRestrict(new String[]{"epub", ""}),
    libraryRestrict(new String[]{"epub", ""}), maxResults(new String[]{"epub", ""}), orderBy(new String[]{"epub", ""}),
    partner(new String[]{"epub", ""}), printType(new String[]{"epub", ""}), projection(new String[]{"epub", ""}),
    showPreorders(new String[]{"epub", ""}), source(new String[]{"epub", ""}), startIndex(new String[]{"epub", ""});

    AdvancedFilter(String[] values) {
    }

    AdvancedFilter(String epub) {
    }
}
